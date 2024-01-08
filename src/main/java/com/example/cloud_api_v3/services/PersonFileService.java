package com.example.cloud_api_v3.services;

import com.example.cloud_api_v3.dto.FileDto;
import com.example.cloud_api_v3.entity.PersonFile;
import com.example.cloud_api_v3.exception.ErrorDeleteFileException;
import com.example.cloud_api_v3.exception.ErrorGettingFileListException;
import com.example.cloud_api_v3.exception.ErrorInputDataException;
import com.example.cloud_api_v3.exception.ErrorUploadFileException;
import com.example.cloud_api_v3.repositories.FileRepository;
import com.example.cloud_api_v3.util.FileUtils;
import com.example.cloud_api_v3.util.MappingDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonFileService {
    private final FileRepository fileRepository;
    private final MappingDto mappingDto;

    public PersonFileService(FileRepository fileRepository, MappingDto mappingDto) {
        this.fileRepository = fileRepository;
        this.mappingDto = mappingDto;
    }

    public void uploadFile(String fileName, MultipartFile file, int personId) {
        if (!fileRepository.findByFileNameAndPersonId(fileName, personId).isEmpty()) {
            throw new ErrorInputDataException("Error input data");
        }
        try {
            fileRepository.save(PersonFile.builder()
                    .fileName(fileName)
                    .personId(personId)
                    .size((int) file.getSize())
                    .fileData(FileUtils.compressImage(file.getBytes())).build());
        } catch (Exception e) {
            throw new ErrorUploadFileException("Error upload file");
        }
    }

    public Resource downloadFile(String fileName, int personId) {

        Optional<PersonFile> personFile;
        try {
            personFile = fileRepository.findByFileNameAndPersonId(fileName, personId);
        } catch (Exception e) {
            throw new ErrorUploadFileException("Error upload file");
        }
        if (personFile.isEmpty()) {
            throw new ErrorInputDataException("Error Input Data");
        }
        try {
            byte[] images = FileUtils.decompressImage(personFile.get().getFileData());
            Resource resource = new ByteArrayResource(images);
            return resource;
        } catch (Exception e) {
            throw new ErrorUploadFileException("Error upload file");
        }

    }

    public void deleteFile(String fileName, int personId) {
        Optional<PersonFile> personFile = fileRepository.findByFileNameAndPersonId(fileName, personId);
        if (personFile.isEmpty()) {
            throw new ErrorInputDataException("Error Input Data");
        }
        try {
            fileRepository.delete(personFile.get());
        } catch (Exception e) {
            throw new ErrorDeleteFileException("Error delete file");
        }
    }

    public void updateFileName(String oldFileName, String newFileName, int personId) throws ErrorUploadFileException {

        Optional<PersonFile> personFileOptional = fileRepository.findByFileNameAndPersonId(oldFileName, personId);
        if (personFileOptional.isEmpty()) {
            throw new ErrorInputDataException("Error Input Data");
        }
        if (!fileRepository.findByFileNameAndPersonId(newFileName, personId).isEmpty()) {
            throw new ErrorInputDataException("Error Input Data");
        }
        try {
            PersonFile personFile = personFileOptional.get();
            personFile.setFileName(newFileName);
            fileRepository.save(personFile);
        } catch (Exception e) {
            throw new ErrorUploadFileException("Error upload file");
        }
    }

    public List<FileDto> getListFiles(int personId, int limit) {
        try {
            Page<PersonFile> page = fileRepository.findAllByPersonId(personId, PageRequest.of(0, limit));

            List<FileDto> listFilesDto = page.stream()
                    .map(file -> mappingDto.personFileToFileDto(file))
                    .collect(Collectors.toList());
            return listFilesDto;
        } catch (Exception e) {
            throw new ErrorGettingFileListException("Error getting file list");
        }
    }
}
