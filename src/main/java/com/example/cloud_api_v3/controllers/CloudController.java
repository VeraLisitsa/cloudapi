package com.example.cloud_api_v3.controllers;

import com.example.cloud_api_v3.dto.FileDto;
import com.example.cloud_api_v3.dto.FileNameDto;
import com.example.cloud_api_v3.services.AuthorisationTokenService;
import com.example.cloud_api_v3.services.PersonFileService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/cloud")
public class CloudController {
    private final AuthorisationTokenService authorisationTokenService;
    private final PersonFileService personFileService;

    @Autowired
    public CloudController(AuthorisationTokenService authorisationTokenService, PersonFileService personFileService) {
        this.authorisationTokenService = authorisationTokenService;
        this.personFileService = personFileService;
    }

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, @RequestBody MultipartFile file) {
        int personId = authorisationTokenService.authorisationConfirmation(authToken, SecurityContextHolder.getContext());
        personFileService.uploadFile(fileName, file, personId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        int personId = authorisationTokenService.authorisationConfirmation(authToken, SecurityContextHolder.getContext());
        Resource resource = personFileService.downloadFile(fileName, personId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(resource);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        int personId = authorisationTokenService.authorisationConfirmation(authToken, SecurityContextHolder.getContext());
        personFileService.deleteFile(fileName, personId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFileName(@RequestHeader("auth-token") String authToken, @RequestBody FileNameDto newFileName, @RequestParam(value = "filename") String oldFileName) {
        int personId = authorisationTokenService.authorisationConfirmation(authToken, SecurityContextHolder.getContext());
        personFileService.updateFileName(oldFileName, newFileName.getFilename(), personId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<FileDto>> downloadListFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") @Min(1) int limit) {
        int personId = authorisationTokenService.authorisationConfirmation(authToken, SecurityContextHolder.getContext());
        List<FileDto> list = personFileService.getListFiles(personId, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(list);
    }

}

