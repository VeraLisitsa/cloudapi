package com.example.cloud_api_v3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person_files")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "person_id")
    private int personId;

    @Column(name = "size")
    private int size;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;
}


