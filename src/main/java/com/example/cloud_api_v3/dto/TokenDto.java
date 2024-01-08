package com.example.cloud_api_v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDto {

    @JsonProperty("auth-token")
    private String authToken;
}
