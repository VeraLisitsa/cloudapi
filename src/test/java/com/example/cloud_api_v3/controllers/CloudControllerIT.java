package com.example.cloud_api_v3.controllers;

import com.example.cloud_api_v3.entity.PersonFile;
import com.example.cloud_api_v3.exception.UnauthorizedErrorException;
import com.example.cloud_api_v3.repositories.FileRepository;
import com.example.cloud_api_v3.services.AuthorisationTokenService;
import com.example.cloud_api_v3.services.PersonFileService;
import com.example.cloud_api_v3.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class CloudControllerIT {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer("postgres");

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthorisationTokenService authorisationTokenServiceMock;

    @Autowired
    private PersonFileService personFileService;

    @Autowired
    private FileRepository fileRepository;

    private byte[] array = "Test".getBytes(StandardCharsets.UTF_8);

    @Test
    @WithUserDetails(value = "user1")
    public void downloadFileNameSuccess() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("testDownLoad1.jpg")
                .personId(1)
                .size(array.length)
                .fileData(FileUtils.compressImage(array)).build());

        var requestBuilder = MockMvcRequestBuilders.get("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("filename", "testDownLoad1.jpg");

        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(1);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.MULTIPART_FORM_DATA),
                        content().bytes(array));

    }

    @Test
    @WithUserDetails(value = "user1")
    public void downloadFileReturnsUnauthorizedError() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("filename", "testDownLoad.jpg");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenThrow(new UnauthorizedErrorException("Unauthorized error"));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = "user1")
    public void downloadFileReturnsErrorDataInput() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("testDownLoad2.jpg")
                .personId(1)
                .size(array.length)
                .fileData(FileUtils.compressImage(array)).build());

        var requestBuilder = MockMvcRequestBuilders.get("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("filename", "test.jpg");

        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(1);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = "user1")
    public void deleteFileSuccess() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("testDelete1.jpg")
                .personId(1)
                .size(array.length)
                .fileData(FileUtils.compressImage(array)).build());

        var requestBuilder = MockMvcRequestBuilders.delete("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("filename", "testDelete1.jpg");

        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(1);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk());

    }

    @Test
    @WithUserDetails(value = "user1")
    public void deleteFileReturnsUnauthorizedError() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("filename", "testDelete.jpg");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenThrow(new UnauthorizedErrorException("Unauthorized error"));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = "user1")
    public void deleteFileReturnsErrorDataInput() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("testDelete2.jpg")
                .personId(1)
                .size(array.length)
                .fileData(FileUtils.compressImage(array)).build());

        var requestBuilder = MockMvcRequestBuilders.delete("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("filename", "test.jpg");

        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(1);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = "user1")
    public void editFileNameSuccess() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("testOld1.jpg")
                .personId(1)
                .size(123)
                .fileData(FileUtils.compressImage(array)).build());
        var requestBuilder = MockMvcRequestBuilders.put("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"filename\":\"testNew.jpg\"}")
                .queryParam("filename", "testOld1.jpg");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(1);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk());

    }

    @Test
    @WithUserDetails(value = "user1")
    public void editFileNameReturnsUnauthorizedError() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.put("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"filename\":\"testNew.jpg\"}")
                .queryParam("filename", "testOld.jpg");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenThrow(new UnauthorizedErrorException("Unauthorized error"));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());

    }

    @Test
    @WithUserDetails(value = "user1")
    public void editFileNameReturnsErrorDataInput() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("testOld2.jpg")
                .personId(1)
                .size(123)
                .fileData(FileUtils.compressImage(array)).build());
        var requestBuilder = MockMvcRequestBuilders.put("/cloud/file")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"filename\":\"testNew.jpg\"}")
                .queryParam("filename", "test.jpg");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(1);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = "user2")
    public void downloadListFilesSuccess() throws Exception {
        fileRepository.save(PersonFile.builder()
                .fileName("test.jpg")
                .personId(2)
                .size(123)
                .fileData(FileUtils.compressImage(array)).build());
        var requestBuilder = MockMvcRequestBuilders.get("/cloud/list")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("limit", "3");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(2);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                [
                                {
                                "filename":"test.jpg",
                                 "size":123
                                }
                                ]
                                """)
                );
    }

    @Test
    @WithUserDetails(value = "user2")
    public void downloadListFilesIncorrectLimitReturnsError() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/cloud/list")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("limit", "0");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenReturn(2);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());

    }

    @Test
    @WithUserDetails(value = "user2")
    public void downloadListFilesReturnsUnauthorizedError() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/cloud/list")
                .header("auth-token", "24ddb9c3-3f88-43aa-955c-9cb3385e3533")
                .queryParam("limit", "3");
        Mockito.when(authorisationTokenServiceMock
                        .authorisationConfirmation(Mockito.anyString(), Mockito.any()))
                .thenThrow(new UnauthorizedErrorException("Unauthorized error"));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(status().is4xxClientError());
    }
}
