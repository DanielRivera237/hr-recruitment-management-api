package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.TechnicalTestRequest;
import com.uca.rrhhbackend.dto.response.TechnicalTestResponse;
import com.uca.rrhhbackend.entity.TechnicalTest;

public final class TechnicalTestMapper {

    private TechnicalTestMapper() {
    }

    public static TechnicalTest toEntity(TechnicalTestRequest request) {
        TechnicalTest test = new TechnicalTest();
        test.setTitle(request.title().trim());
        test.setExternalUrl(request.externalUrl().trim());
        test.setDeadline(request.deadline());
        return test;
    }

    public static TechnicalTestResponse toResponse(TechnicalTest test) {
        return new TechnicalTestResponse(
                test.getId(),
                test.getTitle(),
                test.getExternalUrl(),
                test.getDeadline(),
                test.getStatus(),
                test.getResult(),
                test.getApplication().getId()
        );
    }
}