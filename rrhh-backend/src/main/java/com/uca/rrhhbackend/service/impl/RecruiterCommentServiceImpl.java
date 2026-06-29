package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.RecruiterCommentRequest;
import com.uca.rrhhbackend.dto.response.RecruiterCommentResponse;
import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.entity.RecruiterComment;
import com.uca.rrhhbackend.entity.RecruiterProfile;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.RecruiterCommentMapper;
import com.uca.rrhhbackend.repository.ApplicationRepository;
import com.uca.rrhhbackend.repository.RecruiterCommentRepository;
import com.uca.rrhhbackend.repository.RecruiterProfileRepository;
import com.uca.rrhhbackend.service.RecruiterCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruiterCommentServiceImpl
        implements RecruiterCommentService {

    private final RecruiterCommentRepository commentRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public RecruiterCommentServiceImpl(
            RecruiterCommentRepository commentRepository,
            ApplicationRepository applicationRepository,
            RecruiterProfileRepository recruiterProfileRepository
    ) {
        this.commentRepository = commentRepository;
        this.applicationRepository = applicationRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public RecruiterCommentResponse create(
            RecruiterCommentRequest request
    ) {
        Application application = applicationRepository
                .findById(request.applicationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Postulación no encontrada"
                        )
                );

        RecruiterProfile recruiter = recruiterProfileRepository
                .findById(request.recruiterProfileId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reclutador no encontrado"
                        )
                );

        if (!application.getJobOffer()
                .getCompany()
                .getId()
                .equals(recruiter.getCompany().getId())) {
            throw new BusinessException(
                    "El reclutador no pertenece a la empresa de la vacante"
            );
        }

        RecruiterComment comment =
                RecruiterCommentMapper.toEntity(request);

        comment.setApplication(application);
        comment.setRecruiterProfile(recruiter);

        return RecruiterCommentMapper.toResponse(
                commentRepository.save(comment)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterCommentResponse> findByApplication(
            Long applicationId
    ) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Postulación no encontrada con id " + applicationId
            );
        }

        return commentRepository.findByApplicationId(applicationId)
                .stream()
                .map(RecruiterCommentMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        RecruiterComment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comentario no encontrado con id " + id
                        )
                );

        commentRepository.delete(comment);
    }
}