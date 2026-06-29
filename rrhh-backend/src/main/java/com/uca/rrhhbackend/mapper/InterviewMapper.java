package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.InterviewRequest;
import com.uca.rrhhbackend.dto.response.InterviewResponse;
import com.uca.rrhhbackend.entity.Interview;

public final class InterviewMapper {

    private InterviewMapper() {
    }

    public static Interview toEntity(InterviewRequest request) {
        Interview interview = new Interview();
        interview.setScheduledDate(request.scheduledDate());
        interview.setMeetingUrl(request.meetingUrl().trim());
        interview.setType(request.type());
        return interview;
    }

    public static void updateEntity(
            Interview interview,
            InterviewRequest request
    ) {
        interview.setScheduledDate(request.scheduledDate());
        interview.setMeetingUrl(request.meetingUrl().trim());
        interview.setType(request.type());
    }

    public static InterviewResponse toResponse(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getScheduledDate(),
                interview.getMeetingUrl(),
                interview.getType(),
                interview.getStatus(),
                interview.getApplication().getId(),
                interview.getRecruiterProfile().getId()
        );
    }
}