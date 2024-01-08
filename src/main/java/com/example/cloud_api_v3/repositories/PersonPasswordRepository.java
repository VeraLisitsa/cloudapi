package com.example.cloud_api_v3.repositories;

import com.example.cloud_api_v3.entity.PersonPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonPasswordRepository extends JpaRepository<PersonPassword,Integer> {
}
