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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class PersonFileServiceTest {
    @Mock
    private FileRepository fileRepository;
    private PersonFileService personFileService;

    private ModelMapper modelMapper;
    private MappingDto mappingDto;

    private int personId = 1;

    private String fileName = "1.jpg";

    private byte[] bytes = new byte[4 * 4];
    private MultipartFile multipartFile = new MockMultipartFile(fileName, bytes);

    @BeforeEach
    public void beforeEach() {
        modelMapper = new ModelMapper();
        mappingDto = new MappingDto(modelMapper);
        personFileService = new PersonFileService(fileRepository, mappingDto);
    }

    @AfterEach
    public void afterEach() {
        modelMapper = null;
        mappingDto = null;
        personFileService = null;

    }

    @Test
    public void getListFilesReturnsValidListFilesDto() {
        int limit = 3;
        String fileName2 = "2.jpg";
        Page<PersonFile> page = new PageImpl<>(List.of(new PersonFile(1, fileName, personId, 123, null),
                new PersonFile(2, fileName2, personId, 234, null)));
        List<FileDto> expectedListFiles = List.of(new FileDto(fileName, 123), new FileDto(fileName2, 234));

        Mockito.when(fileRepository.findAllByPersonId(personId, PageRequest.of(0, limit))).thenReturn(page);

        List<FileDto> listFiles = this.personFileService.getListFiles(personId, limit);

        Assertions.assertNotNull(listFiles);
        Assertions.assertEquals(listFiles, expectedListFiles);
    }

    @Test
    public void getListFilesReturnsError() {
        int limit = 3;
        Mockito.when(fileRepository.findAllByPersonId(personId, PageRequest.of(0, limit)))
                .thenThrow(new RuntimeException());

        Class<ErrorGettingFileListException> expected = ErrorGettingFileListException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.getListFiles(personId, limit));
    }

    @Test
    public void uploadFileSuccess() throws ErrorUploadFileException {
        Optional<PersonFile> personFileExisted = Optional.ofNullable(null);

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileExisted);

        personFileService.uploadFile(fileName, multipartFile, personId);

        Mockito.verify(fileRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    public void uploadFileReturnsErrorInputData() {
        Optional<PersonFile> personFileNull = Optional.of(new PersonFile());

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileNull);

        Class<ErrorInputDataException> expected = ErrorInputDataException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.uploadFile(fileName, multipartFile, personId));

    }

    @Test
    public void uploadFileReturnsErrorUploadFile() {
        Optional<PersonFile> personFileNull = Optional.ofNullable(null);

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileNull);

        Mockito.when(fileRepository.save(Mockito.any()))
                .thenThrow(new RuntimeException());

        Class<ErrorUploadFileException> expected = ErrorUploadFileException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.uploadFile(fileName, multipartFile, personId));
    }

    @Test
    public void downloadFileSuccess() {
        PersonFile personFile = PersonFile.builder()
                .fileName(fileName)
                .personId(personId)
                .fileData(FileUtils.compressImage(bytes))
                .build();
        Optional<PersonFile> personFileOptional = Optional.of(personFile);

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileOptional);

        Resource resourceExpected = new ByteArrayResource(bytes);

        Resource resource = personFileService.downloadFile(fileName, personId);

        Assertions.assertEquals(resourceExpected, resource);

    }

    @Test
    public void downloadFileReturnsErrorInputData() {
        Optional<PersonFile> personFileNull = Optional.ofNullable(null);
        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileNull);

        Class<ErrorInputDataException> expected = ErrorInputDataException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.downloadFile(fileName, personId));

    }

    @Test
    public void downLoadFileReturnsErrorUploadFile() {

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenThrow(new RuntimeException());

        Class<ErrorUploadFileException> expected = ErrorUploadFileException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.downloadFile(fileName, personId));

    }

    @Test
    public void deleteFileSuccess() {
        PersonFile personFile = Mockito.mock(PersonFile.class);
        Optional<PersonFile> personFileOptional = Optional.of(personFile);

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileOptional);
        personFileService.deleteFile(fileName, personId);

        Mockito.verify(fileRepository, Mockito.times(1)).delete(Mockito.any());
        Mockito.verify(fileRepository).delete(personFile);

    }

    @Test
    public void deleteFileReturnsErrorInputData() {
        Optional<PersonFile> personFileNull = Optional.ofNullable(null);
        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileNull);

        Class<ErrorInputDataException> expected = ErrorInputDataException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.deleteFile(fileName, personId));

    }

    @Test
    public void deleteFileReturnsErrorDeleteFile() {
        PersonFile personFile = Mockito.mock(PersonFile.class);
        Optional<PersonFile> personFileOptional = Optional.of(personFile);

        Mockito.when(fileRepository.findByFileNameAndPersonId(fileName, personId)).thenReturn(personFileOptional);
        personFileService.deleteFile(fileName, personId);

        Mockito.doThrow(new RuntimeException()).when(fileRepository).delete(Mockito.any());

        Class<ErrorDeleteFileException> expected = ErrorDeleteFileException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.deleteFile(fileName, personId));
    }

    @Test
    public void updateFileNameSuccess() {
        String oldFileName = fileName;
        String newFileName = "2.jpg";

        Optional<PersonFile> personFileOptional = Optional.of(new PersonFile());

        Mockito.when(fileRepository.findByFileNameAndPersonId(oldFileName, personId)).thenReturn(personFileOptional);

        Mockito.when(fileRepository.findByFileNameAndPersonId(newFileName, personId)).thenReturn(Optional.ofNullable(null));
        personFileService.updateFileName(oldFileName, newFileName, personId);

        Mockito.verify(fileRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(fileRepository).save(personFileOptional.get());
    }

    @Test
    public void updateFileReturnsErrorInputDataOldFileName() {
        String oldFileName = fileName;
        String newFileName = "2.jpg";

        Mockito.when(fileRepository.findByFileNameAndPersonId(oldFileName, personId)).thenReturn(Optional.ofNullable(null));

        Class<ErrorInputDataException> expected = ErrorInputDataException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.updateFileName(oldFileName, newFileName, personId));

    }

    @Test
    public void updateFileReturnsErrorInputDataNewFileName() {
        String oldFileName = fileName;
        String newFileName = "2.jpg";

        Mockito.when(fileRepository.findByFileNameAndPersonId(oldFileName, personId)).thenReturn(Optional.of(new PersonFile()));

        Mockito.when(fileRepository.findByFileNameAndPersonId(newFileName, personId)).thenReturn(Optional.of(new PersonFile()));

        Class<ErrorInputDataException> expected = ErrorInputDataException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.updateFileName(oldFileName, newFileName, personId));
    }

    @Test
    public void updateFileReturnsErrorUploadFile() {
        String oldFileName = fileName;
        String newFileName = "2.jpg";
        Mockito.when(fileRepository.findByFileNameAndPersonId(oldFileName, personId)).thenReturn(Optional.of(new PersonFile()));

        Mockito.when(fileRepository.findByFileNameAndPersonId(newFileName, personId)).thenReturn(Optional.ofNullable(null));
        Mockito.when(fileRepository.save(Mockito.any())).thenThrow(new RuntimeException());
        Class<ErrorUploadFileException> expected = ErrorUploadFileException.class;

        Assertions.assertThrows(expected, () -> this.personFileService.updateFileName(oldFileName, newFileName, personId));
    }
}
