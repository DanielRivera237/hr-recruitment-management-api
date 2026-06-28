package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.RecruiterCommentRequest;
import com.uca.rrhhbackend.dto.response.RecruiterCommentResponse;
import com.uca.rrhhbackend.entity.RecruiterComment;

public final class RecruiterCommentMapper {

    private RecruiterCommentMapper() {
    }

    public static RecruiterComment toEntity(RecruiterCommentRequest request) {
        RecruiterComment comment = new RecruiterComment();
        comment.setComment(request.comment().trim());
        return comment;
    }

    public static RecruiterCommentResponse toResponse(
            RecruiterComment comment
    ) {
        return new RecruiterCommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getApplication().getId(),
                comment.getRecruiterProfile().getId()
        );
    }
}