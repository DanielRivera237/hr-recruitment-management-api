package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findByCandidateProfileId(Long candidateProfileId);


    Optional<Education> findByIdAndCandidateProfileId(
            Long id,
            Long candidateProfileId );
}