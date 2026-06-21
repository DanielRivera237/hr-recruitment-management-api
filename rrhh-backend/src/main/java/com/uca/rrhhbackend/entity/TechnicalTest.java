package com.uca.rrhhbackend.entity;

import com.uca.rrhhbackend.entity.enums.TechnicalTestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "technical_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String externalUrl;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TechnicalTestStatus status = TechnicalTestStatus.PENDING;

    @Column(precision = 5, scale = 2)
    private BigDecimal result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
}