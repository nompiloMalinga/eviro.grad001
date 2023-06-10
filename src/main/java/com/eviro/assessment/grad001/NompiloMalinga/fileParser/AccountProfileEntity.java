package com.eviro.assessment.grad001.NompiloMalinga.fileParser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AccountProfile")

public class AccountProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false,unique = true, length = 20)
    private String name;
    @Column( name = "surname",nullable = false, length = 20)
    private String surname;
    @Column( name = "httpImageLink",nullable = false)
    private String httpImageLink;



}
