package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.RecruiterComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruiterCommentRepository
        extends JpaRepository<RecruiterComment, Long> {

    List<RecruiterComment> findByApplicationId(Long applicationId);

    List<RecruiterComment> findByRecruiterProfileId(Long recruiterProfileId);
}