package com.matheustorres.gestao_vagas.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheustorres.gestao_vagas.models.CandidateModel;
import com.matheustorres.gestao_vagas.records.AuthCandidateRequestRecord;
import com.matheustorres.gestao_vagas.services.CandidateService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateModel candidateModel) {
        try {
            var result = candidateService.save(candidateModel);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody AuthCandidateRequestRecord authCandidateRequestRecord) {
        try {
            var token = this.candidateService.authenticateCandidate(authCandidateRequestRecord);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> get(HttpServletRequest request) {
        var idCandidate = request.getAttribute("idCandidate");

        try {
            var profile = this.candidateService
                    .findCandidateProfile(UUID.fromString(idCandidate.toString()));
            return ResponseEntity.ok().body(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
