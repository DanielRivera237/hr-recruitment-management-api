package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruiterProfileRepository
        extends JpaRepository<RecruiterProfile, Long> {

    Optional<RecruiterProfile> findByUserId(Long userId);

    List<RecruiterProfile> findByCompanyId(Long companyId);

    boolean existsByUserId(Long userId);
}