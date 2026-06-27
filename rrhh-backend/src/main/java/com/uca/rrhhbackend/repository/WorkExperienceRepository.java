package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkExperienceRepository
        extends JpaRepository<WorkExperience, Long> {

    List<WorkExperience> findByCandidateProfileId(Long candidateProfileId);

    Optional<WorkExperience> findByIdAndCandidateProfileId(
            Long id,
            Long candidateProfileId
    );
}