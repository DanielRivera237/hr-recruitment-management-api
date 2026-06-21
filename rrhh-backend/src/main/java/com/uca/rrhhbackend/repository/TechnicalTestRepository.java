package com.uca.rrhhbackend.repository;

import com.uca.rrhhbackend.entity.TechnicalTest;
import com.uca.rrhhbackend.entity.enums.TechnicalTestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnicalTestRepository
        extends JpaRepository<TechnicalTest, Long> {

    List<TechnicalTest> findByApplicationId(Long applicationId);

    List<TechnicalTest> findByStatus(TechnicalTestStatus status);
}