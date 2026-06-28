package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.TechnicalTestRequest;
import com.uca.rrhhbackend.dto.request.TechnicalTestResultRequest;
import com.uca.rrhhbackend.dto.response.TechnicalTestResponse;
import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.entity.TechnicalTest;
import com.uca.rrhhbackend.entity.enums.ApplicationStatus;
import com.uca.rrhhbackend.entity.enums.TechnicalTestStatus;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.TechnicalTestMapper;
import com.uca.rrhhbackend.repository.ApplicationRepository;
import com.uca.rrhhbackend.repository.TechnicalTestRepository;
import com.uca.rrhhbackend.service.TechnicalTestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TechnicalTestServiceImpl implements TechnicalTestService {

    private final TechnicalTestRepository technicalTestRepository;
    private final ApplicationRepository applicationRepository;

    public TechnicalTestServiceImpl(
            TechnicalTestRepository technicalTestRepository,
            ApplicationRepository applicationRepository
    ) {
        this.technicalTestRepository = technicalTestRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public TechnicalTestResponse create(TechnicalTestRequest request) {
        Application application = applicationRepository
                .findById(request.applicationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Postulación no encontrada"
                        )
                );

        if (application.getStatus()
                != ApplicationStatus.TECHNICAL_INTERVIEW) {
            throw new BusinessException(
                    "La postulación debe estar en entrevista técnica"
            );
        }

        TechnicalTest test = TechnicalTestMapper.toEntity(request);
        test.setApplication(application);
        test.setStatus(TechnicalTestStatus.PENDING);

        return TechnicalTestMapper.toResponse(
                technicalTestRepository.save(test)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TechnicalTestResponse findById(Long id) {
        return TechnicalTestMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicalTestResponse> findByApplication(
            Long applicationId
    ) {
        return technicalTestRepository.findByApplicationId(applicationId)
                .stream()
                .map(TechnicalTestMapper::toResponse)
                .toList();
    }

    @Override
    public TechnicalTestResponse submit(Long id) {
        TechnicalTest test = findEntity(id);

        if (test.getStatus() != TechnicalTestStatus.PENDING) {
            throw new BusinessException(
                    "La prueba técnica no está pendiente"
            );
        }

        if (test.getDeadline().isBefore(LocalDateTime.now())) {
            test.setStatus(TechnicalTestStatus.EXPIRED);
            technicalTestRepository.save(test);

            throw new BusinessException(
                    "La fecha límite de la prueba técnica ya venció"
            );
        }

        test.setStatus(TechnicalTestStatus.SUBMITTED);

        return TechnicalTestMapper.toResponse(
                technicalTestRepository.save(test)
        );
    }

    @Override
    public TechnicalTestResponse review(
            Long id,
            TechnicalTestResultRequest request
    ) {
        TechnicalTest test = findEntity(id);

        if (test.getStatus() != TechnicalTestStatus.SUBMITTED) {
            throw new BusinessException(
                    "Solo se pueden revisar pruebas enviadas"
            );
        }

        test.setResult(request.result());
        test.setStatus(TechnicalTestStatus.REVIEWED);

        return TechnicalTestMapper.toResponse(
                technicalTestRepository.save(test)
        );
    }

    private TechnicalTest findEntity(Long id) {
        return technicalTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prueba técnica no encontrada con id " + id
                        )
                );
    }
}