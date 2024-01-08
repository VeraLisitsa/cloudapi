package com.example.cloud_api_v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudApiV3Application {

	public static void main(String[] args) {
		SpringApplication.run(CloudApiV3Application.class, args);
	}
	@Bean
	public ModelMapper mapperBean() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapperBean(){
		return new ObjectMapper();
	}

}
