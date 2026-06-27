package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.JobOfferRequest;
import com.uca.rrhhbackend.dto.response.JobOfferResponse;
import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.Skill;

import java.util.stream.Collectors;

public final class JobOfferMapper {

    private JobOfferMapper() {
    }

    public static JobOffer toEntity(JobOfferRequest request) {
        JobOffer jobOffer = new JobOffer();

        jobOffer.setTitle(request.title().trim());
        jobOffer.setDescription(request.description().trim());
        jobOffer.setRequirements(request.requirements().trim());
        jobOffer.setSalaryMin(request.salaryMin());
        jobOffer.setSalaryMax(request.salaryMax());
        jobOffer.setModality(request.modality());
        jobOffer.setLocation(request.location().trim());
        jobOffer.setClosingDate(request.closingDate());

        return jobOffer;
    }

    public static void updateEntity(JobOffer jobOffer, JobOfferRequest request) {
        jobOffer.setTitle(request.title().trim());
        jobOffer.setDescription(request.description().trim());
        jobOffer.setRequirements(request.requirements().trim());
        jobOffer.setSalaryMin(request.salaryMin());
        jobOffer.setSalaryMax(request.salaryMax());
        jobOffer.setModality(request.modality());
        jobOffer.setLocation(request.location().trim());
        jobOffer.setClosingDate(request.closingDate());
    }

    public static JobOfferResponse toResponse(JobOffer jobOffer) {
        return new JobOfferResponse(
                jobOffer.getId(),
                jobOffer.getTitle(),
                jobOffer.getDescription(),
                jobOffer.getRequirements(),
                jobOffer.getSalaryMin(),
                jobOffer.getSalaryMax(),
                jobOffer.getModality(),
                jobOffer.getLocation(),
                jobOffer.getStatus(),
                jobOffer.getPublicationDate(),
                jobOffer.getClosingDate(),
                jobOffer.getCreatedAt(),
                jobOffer.getCompany().getId(),
                jobOffer.getCompany().getName(),
                jobOffer.getRecruiterProfile().getId(),
                jobOffer.getRequiredSkills()
                        .stream()
                        .map(Skill::getName)
                        .collect(Collectors.toSet())
        );
    }
}