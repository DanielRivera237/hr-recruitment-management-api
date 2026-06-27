package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.JobOfferRequest;
import com.uca.rrhhbackend.dto.response.JobOfferResponse;
import com.uca.rrhhbackend.entity.*;
import com.uca.rrhhbackend.entity.enums.JobOfferStatus;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.JobOfferMapper;
import com.uca.rrhhbackend.repository.*;
import com.uca.rrhhbackend.service.JobOfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final SkillRepository skillRepository;

    public JobOfferServiceImpl(
            JobOfferRepository jobOfferRepository,
            CompanyRepository companyRepository,
            RecruiterProfileRepository recruiterProfileRepository,
            SkillRepository skillRepository
    ) {
        this.jobOfferRepository = jobOfferRepository;
        this.companyRepository = companyRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public JobOfferResponse create(JobOfferRequest request) {
        validateSalary(request);
        validateClosingDate(request.closingDate());

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        RecruiterProfile recruiter = recruiterProfileRepository
                .findById(request.recruiterProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Reclutador no encontrado"));

        if (!recruiter.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("El reclutador no pertenece a la empresa indicada");
        }

        JobOffer jobOffer = JobOfferMapper.toEntity(request);
        jobOffer.setCompany(company);
        jobOffer.setRecruiterProfile(recruiter);
        jobOffer.setStatus(JobOfferStatus.DRAFT);
        jobOffer.setRequiredSkills(findSkills(request.skillIds()));

        return JobOfferMapper.toResponse(jobOfferRepository.save(jobOffer));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobOfferResponse> findAll() {
        return jobOfferRepository.findAll()
                .stream()
                .map(JobOfferMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobOfferResponse findById(Long id) {
        return JobOfferMapper.toResponse(findEntity(id));
    }

    @Override
    public JobOfferResponse update(Long id, JobOfferRequest request) {
        validateSalary(request);
        validateClosingDate(request.closingDate());

        JobOffer jobOffer = findEntity(id);

        if (jobOffer.getStatus() != JobOfferStatus.DRAFT) {
            throw new BusinessException("Solo se pueden editar vacantes en estado DRAFT");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        RecruiterProfile recruiter = recruiterProfileRepository
                .findById(request.recruiterProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Reclutador no encontrado"));

        if (!recruiter.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("El reclutador no pertenece a la empresa indicada");
        }

        JobOfferMapper.updateEntity(jobOffer, request);
        jobOffer.setCompany(company);
        jobOffer.setRecruiterProfile(recruiter);
        jobOffer.setRequiredSkills(findSkills(request.skillIds()));

        return JobOfferMapper.toResponse(jobOfferRepository.save(jobOffer));
    }

    @Override
    public JobOfferResponse publish(Long id) {
        JobOffer jobOffer = findEntity(id);

        if (jobOffer.getStatus() != JobOfferStatus.DRAFT) {
            throw new BusinessException("Solo se pueden publicar vacantes en estado DRAFT");
        }

        jobOffer.setStatus(JobOfferStatus.PUBLISHED);
        jobOffer.setPublicationDate(LocalDate.now());

        return JobOfferMapper.toResponse(jobOfferRepository.save(jobOffer));
    }

    @Override
    public JobOfferResponse close(Long id) {
        JobOffer jobOffer = findEntity(id);

        if (jobOffer.getStatus() != JobOfferStatus.PUBLISHED) {
            throw new BusinessException("Solo se pueden cerrar vacantes publicadas");
        }

        jobOffer.setStatus(JobOfferStatus.CLOSED);

        return JobOfferMapper.toResponse(jobOfferRepository.save(jobOffer));
    }

    @Override
    public void cancel(Long id) {
        JobOffer jobOffer = findEntity(id);

        if (jobOffer.getStatus() == JobOfferStatus.CLOSED) {
            throw new BusinessException("Una vacante cerrada no puede cancelarse");
        }

        jobOffer.setStatus(JobOfferStatus.CANCELLED);
        jobOfferRepository.save(jobOffer);
    }

    private JobOffer findEntity(Long id) {
        return jobOfferRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vacante no encontrada con id " + id)
                );
    }

    private Set<Skill> findSkills(Set<Long> skillIds) {
        if (skillIds == null || skillIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Skill> skills = skillRepository.findAllById(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new ResourceNotFoundException("Una o más habilidades no existen");
        }

        return new HashSet<>(skills);
    }

    private void validateSalary(JobOfferRequest request) {
        if (request.salaryMin().compareTo(request.salaryMax()) > 0) {
            throw new BusinessException(
                    "El salario mínimo no puede ser mayor que el salario máximo"
            );
        }
    }

    private void validateClosingDate(LocalDate closingDate) {
        if (!closingDate.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de cierre debe ser futura");
        }
    }
}