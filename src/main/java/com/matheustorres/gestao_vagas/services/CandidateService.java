package com.matheustorres.gestao_vagas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheustorres.gestao_vagas.exceptions.UserFoundException;
import com.matheustorres.gestao_vagas.models.CandidateModel;
import com.matheustorres.gestao_vagas.repositories.CandidateRepository;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateModel save(CandidateModel candidateModel) {
        this.candidateRepository.findByUsernameOrEmail(candidateModel.getUsername(),
                candidateModel.getEmail()).ifPresent(user -> {
                    throw new UserFoundException();
                });

        return this.candidateRepository.save(candidateModel);
    }
}
