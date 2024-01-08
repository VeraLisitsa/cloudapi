package com.example.cloud_api_v3.util;


import com.example.cloud_api_v3.dto.ErrorDto;
import com.example.cloud_api_v3.dto.FileDto;
import com.example.cloud_api_v3.dto.TokenDto;
import com.example.cloud_api_v3.entity.ErrorEntity;
import com.example.cloud_api_v3.entity.Person;
import com.example.cloud_api_v3.entity.PersonFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MappingDto {
    private final ModelMapper mapper;

    @Autowired
    public MappingDto(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ErrorDto errorEntityToErrorDto(ErrorEntity errorEntity) {
        return (Objects.isNull(errorEntity) ? null : mapper.map(errorEntity, ErrorDto.class));
    }

    public FileDto personFileToFileDto(PersonFile personFile) {
        return (Objects.isNull(personFile) ? null : mapper.map(personFile, FileDto.class));
    }

    public TokenDto personToTokenDto(Person person) {
        return (Objects.isNull(person) ? null : mapper.map(person, TokenDto.class));
    }

}