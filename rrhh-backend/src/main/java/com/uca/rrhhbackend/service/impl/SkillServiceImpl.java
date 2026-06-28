package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.SkillRequest;
import com.uca.rrhhbackend.dto.response.SkillResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.Skill;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.SkillMapper;
import com.uca.rrhhbackend.repository.CandidateProfileRepository;
import com.uca.rrhhbackend.repository.SkillRepository;
import com.uca.rrhhbackend.service.CandidateProfileService;
import com.uca.rrhhbackend.service.SkillService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateProfileService candidateProfileService;

    public SkillServiceImpl(
            SkillRepository skillRepository,
            CandidateProfileRepository candidateProfileRepository,
            CandidateProfileService candidateProfileService
    ) {

        this.skillRepository = skillRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.candidateProfileService = candidateProfileService;
    }

    @Override
    public SkillResponse addSkill(
            User currentUser,
            SkillRequest request
    ) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        String normalizedName = normalizeSkillName(request.name());

        Skill skill = skillRepository.findByNameIgnoreCase(normalizedName)
                .orElseGet(() -> skillRepository.save(SkillMapper.toEntity(normalizedName)));

        boolean alreadyAssigned = profile.getSkills()
                .stream()
                .anyMatch(existingSkill -> existingSkill.getId().equals(skill.getId()));

        if (alreadyAssigned) {
            throw new BusinessException("El candidato ya tiene esa habilidad");
        }

        profile.getSkills().add(skill);

        candidateProfileRepository.save(profile);

        return SkillMapper.toResponse(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillResponse> findMySkills(User currentUser) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        return profile.getSkills()
                .stream()
                .map(SkillMapper::toResponse)
                .toList();
    }

    @Override
    public void removeSkill(
            User currentUser,
            Long skillId
    ) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        boolean removed = profile.getSkills()
                .removeIf(skill -> skill.getId().equals(skillId));

        if (!removed) {
            throw new ResourceNotFoundException(
                    "La habilidad no está asociada a este candidato"
            );
        }

        candidateProfileRepository.save(profile);
    }

    private String normalizeSkillName(String name) {

        if (name == null || name.isBlank()) {
            throw new BusinessException("Debe colocar nombre a la habilidad");
        }

        return name.trim().replaceAll("\\s+", " ");
    }

}