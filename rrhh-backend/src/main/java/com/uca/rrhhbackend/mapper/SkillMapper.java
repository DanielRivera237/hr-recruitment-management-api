package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.response.SkillResponse;
import com.uca.rrhhbackend.entity.Skill;

public final class SkillMapper {

    private SkillMapper() {

    }

    public static Skill toEntity(String name) {

        Skill skill = new Skill();

        skill.setName(name.trim().replaceAll("\\s+", " "));
        skill.setActive(true);

        return skill;
    }

    public static SkillResponse toResponse(Skill skill) {

        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getActive()
        );
    }

}