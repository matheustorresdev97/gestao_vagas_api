package com.matheustorres.gestao_vagas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheustorres.gestao_vagas.models.CompanyModel;
import com.matheustorres.gestao_vagas.records.AuthCompanyRecord;
import com.matheustorres.gestao_vagas.services.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CompanyModel companyModel) {
        try {
            var result = this.companyService.save(companyModel);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody AuthCompanyRecord authCompanyRecord) {
        try {
            var result = this.companyService.execute(authCompanyRecord);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
