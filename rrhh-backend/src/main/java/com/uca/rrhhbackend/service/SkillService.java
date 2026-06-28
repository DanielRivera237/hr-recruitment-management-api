package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.SkillRequest;
import com.uca.rrhhbackend.dto.response.SkillResponse;
import com.uca.rrhhbackend.entity.User;

import java.util.List;

public interface SkillService {
    SkillResponse addSkill(User currentUser, SkillRequest request);

    List<SkillResponse> findMySkills(User currentUser);

    void removeSkill(User currentUser, Long skillId);
}