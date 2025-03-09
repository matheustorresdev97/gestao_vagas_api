package com.matheustorres.gestao_vagas.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheustorres.gestao_vagas.models.JobModel;
import com.matheustorres.gestao_vagas.records.CreateJobRecord;
import com.matheustorres.gestao_vagas.services.JobService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateJobRecord createJobRecord,
            HttpServletRequest request) {
        try {
            var companyId = request.getAttribute("company_id");

            var jobEntity = JobModel.builder()
                    .benefits(createJobRecord.benefits())
                    .companyId(UUID.fromString(companyId.toString()))
                    .description(createJobRecord.description())
                    .level(createJobRecord.level())
                    .build();

            var result = this.jobService.save(jobEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
