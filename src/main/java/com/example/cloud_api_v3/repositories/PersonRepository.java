package com.example.cloud_api_v3.repositories;

import com.example.cloud_api_v3.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    public Optional<Person> findByUsername(String username);
}
