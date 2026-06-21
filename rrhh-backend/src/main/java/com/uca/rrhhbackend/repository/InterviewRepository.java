package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.Interview;
import com.uca.rrhhbackend.entity.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationId(Long applicationId);

    List<Interview> findByRecruiterProfileId(Long recruiterProfileId);

    List<Interview> findByStatus(InterviewStatus status);
}