package com.matheustorres.gestao_vagas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matheustorres.gestao_vagas.models.JobModel;
import com.matheustorres.gestao_vagas.repositories.JobRepository;

@Service
public class JobService {

    private JobRepository jobRepository;

    public JobModel save(JobModel jobModel) {
        return this.jobRepository.save(jobModel);
    }

    public List<JobModel> execute(String filter) {
        return this.jobRepository.findByDescriptionContainingIgnoreCase(filter);
    }
}
