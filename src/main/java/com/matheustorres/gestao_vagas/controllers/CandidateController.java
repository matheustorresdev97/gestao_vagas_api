package com.matheustorres.gestao_vagas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheustorres.gestao_vagas.models.CandidateModel;
import com.matheustorres.gestao_vagas.repositories.CandidateRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @PostMapping("/")
    public CandidateModel create(@Valid @RequestBody CandidateModel candidateModel) {
        return this.candidateRepository.save(candidateModel);
    }
}
