package com.matheustorres.gestao_vagas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheustorres.gestao_vagas.exceptions.UserFoundException;
import com.matheustorres.gestao_vagas.models.CompanyModel;
import com.matheustorres.gestao_vagas.repositories.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyModel save(CompanyModel companyModel) {
        this.companyRepository
                .findByUsernameOrEmail(companyModel.getUsername(), companyModel.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        return this.companyRepository.save(companyModel);
    }
}
