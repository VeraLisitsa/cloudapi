package com.example.cloud_api_v3.repositories;

import com.example.cloud_api_v3.entity.PersonFile;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<PersonFile, Integer> {

    public Optional<PersonFile> findByFileNameAndPersonId(String fileName, int personId);

    public Page<PersonFile> findAllByPersonId(int personId, Pageable page);
}
