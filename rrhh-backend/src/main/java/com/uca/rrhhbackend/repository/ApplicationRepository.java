package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByCandidateProfileId(Long candidateProfileId);

    List<Application> findByJobOfferId(Long jobOfferId);

    List<Application> findByStatus(ApplicationStatus status);

    boolean existsByCandidateProfileIdAndJobOfferId(
            Long candidateProfileId,
            Long jobOfferId
    );
}