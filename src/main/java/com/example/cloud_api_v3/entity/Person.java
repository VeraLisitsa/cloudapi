package com.example.cloud_api_v3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="persons")
public class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "password_id")
    private PersonPassword personPassword;

    @Transient
    private String authToken;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="person_id")
    private List<PersonFile> files;
}
