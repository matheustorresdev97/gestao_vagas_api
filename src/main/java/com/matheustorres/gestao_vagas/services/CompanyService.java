package com.matheustorres.gestao_vagas.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.matheustorres.gestao_vagas.exceptions.UserFoundException;
import com.matheustorres.gestao_vagas.models.CompanyModel;
import com.matheustorres.gestao_vagas.records.AuthCompanyRecord;
import com.matheustorres.gestao_vagas.records.AuthCompanyResponseDTO;
import com.matheustorres.gestao_vagas.repositories.CompanyRepository;

@Service
public class CompanyService {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyModel save(CompanyModel companyModel) {
        this.companyRepository
                .findByUsernameOrEmail(companyModel.getUsername(), companyModel.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        var password = passwordEncoder.encode(companyModel.getPassword());
        companyModel.setPassword(password);

        return this.companyRepository.save(companyModel);
    }

    public AuthCompanyResponseDTO execute(AuthCompanyRecord authCompanyRecord) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyRecord.username()).orElseThrow(() -> {
            throw new UsernameNotFoundException("Username/password incorrect");
        });

        var passwordMatches = this.passwordEncoder.matches(authCompanyRecord.password(), company.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid credentials");
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));

        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiresIn)
                .withSubject(company.getId().toString())
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);

        var authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();

        return authCompanyResponseDTO;

    }
}
