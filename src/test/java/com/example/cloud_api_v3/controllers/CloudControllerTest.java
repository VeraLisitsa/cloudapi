package com.example.cloud_api_v3.controllers;

import com.example.cloud_api_v3.dto.FileDto;
import com.example.cloud_api_v3.dto.FileNameDto;
import com.example.cloud_api_v3.services.AuthorisationTokenService;
import com.example.cloud_api_v3.services.PersonFileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CloudControllerTest {
    @Mock
    private AuthorisationTokenService authorisationTokenService;
    @Mock
    private PersonFileService personFileService;
    @InjectMocks
    private CloudController cloudController;
    private int personId = 1;
    private String fileName = "1.jpg";

    @BeforeEach
    public void beforeEach() {
        Mockito.when(authorisationTokenService.authorisationConfirmation(Mockito.any(), Mockito.any())).thenReturn(personId);
    }

    @Test
    public void uploadFileSuccess() {
        MultipartFile file = Mockito.mock(MultipartFile.class);

        Mockito.doNothing().when(personFileService).uploadFile(fileName, file, personId);

        ResponseEntity<?> responseEntity = this.cloudController.uploadFile(UUID.randomUUID().toString(), fileName, file);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void downloadFileSuccess() {
        Mockito.when(personFileService.downloadFile(fileName, personId)).thenReturn(new ByteArrayResource(new byte[4 * 4]));

        ResponseEntity<?> responseEntity = this.cloudController.downloadFile(UUID.randomUUID().toString(), fileName);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteFileSuccess() {
        Mockito.doNothing().when(personFileService).deleteFile(fileName, personId);

        ResponseEntity<?> responseEntity = this.cloudController.deleteFile(UUID.randomUUID().toString(), fileName);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void editFileNameSuccess() {
        String oldFileName = fileName;
        String newFileName = "2.jpg";
        FileNameDto newFileNameDto = Mockito.mock(FileNameDto.class);
        Mockito.when(newFileNameDto.getFilename()).thenReturn(newFileName);
        doNothing().when(personFileService).updateFileName(oldFileName, newFileName, personId);

        ResponseEntity<?> responseEntity = this.cloudController.editFileName(UUID.randomUUID().toString(), newFileNameDto, oldFileName);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void downloadListFilesSuccess() {
        int limit = 3;
        int sizeExpected = 2;
        List<FileDto> listFiles = List.of(new FileDto("1.jpg", 123), new FileDto("2.jpg", 234));
        Mockito.when(personFileService.getListFiles(personId, limit)).thenReturn(listFiles);

        ResponseEntity<?> responseEntity = this.cloudController.downloadListFiles(UUID.randomUUID().toString(), limit);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        if (responseEntity.getBody() instanceof List list) {
            Assertions.assertEquals(sizeExpected, list.size());
        }

    }
}
