package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.enums.JobOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    List<JobOffer> findByCompanyId(Long companyId);

    List<JobOffer> findByStatus(JobOfferStatus status);

    List<JobOffer> findByRecruiterProfileId(Long recruiterProfileId);
}