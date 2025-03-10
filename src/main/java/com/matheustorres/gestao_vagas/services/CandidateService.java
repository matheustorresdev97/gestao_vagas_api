package com.matheustorres.gestao_vagas.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.matheustorres.gestao_vagas.exceptions.UserFoundException;
import com.matheustorres.gestao_vagas.models.CandidateModel;
import com.matheustorres.gestao_vagas.records.AuthCandidateRequestRecord;
import com.matheustorres.gestao_vagas.records.AuthCandidateResponseRecord;
import com.matheustorres.gestao_vagas.records.ProfileCandidateResponseDTO;
import com.matheustorres.gestao_vagas.repositories.CandidateRepository;

@Service
public class CandidateService {

    @Value("${security.token.secret.candidate}")
    private String secretKey;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateModel save(CandidateModel candidateModel) {
        this.candidateRepository.findByUsernameOrEmail(candidateModel.getUsername(),
                candidateModel.getEmail()).ifPresent(user -> {
                    throw new UserFoundException();
                });

        var password = passwordEncoder.encode(candidateModel.getPassword());
        candidateModel.setPassword(password);

        return this.candidateRepository.save(candidateModel);
    }

    public AuthCandidateResponseRecord authenticateCandidate(AuthCandidateRequestRecord authCandidateRequestRecord) {
        var candidate = this.candidateRepository.findByUsername(authCandidateRequestRecord.username())
                .orElseThrow(() -> new UsernameNotFoundException("Username/password incorrect"));

        var passwordMatches = this.passwordEncoder
                .matches(authCandidateRequestRecord.password(), candidate.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException("Username/password incorrect");
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", Arrays.asList("CANDIDATE"))
                .withExpiresAt(expiresIn)

                .sign(algorithm);

        return new AuthCandidateResponseRecord(token, expiresIn.toEpochMilli());
    }

    public ProfileCandidateResponseDTO findCandidateProfile(UUID idCandidate) {
        var candidate = this.candidateRepository.findById(idCandidate)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var candidateRecord = ProfileCandidateResponseDTO.builder()
                .description(candidate.getDescription())
                .email(candidate.getEmail())
                .username(candidate.getUsername())
                .name(candidate.getName())
                .id(candidate.getId())
                .build();

        return candidateRecord;
    }

}
