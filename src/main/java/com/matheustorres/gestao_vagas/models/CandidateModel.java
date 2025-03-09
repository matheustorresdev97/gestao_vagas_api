package com.matheustorres.gestao_vagas.models;

import java.util.UUID;

import lombok.Data;

@Data
public class CandidateModel {

    private UUID id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String description;
    private String curriculum;
}
