package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.CandidateProfileRequest;
import com.uca.rrhhbackend.dto.response.CandidateProfileResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.User;

public interface CandidateProfileService {
    CandidateProfileResponse createOrUpdateMyProfile(
            User currentUser,
            CandidateProfileRequest request
    );

    CandidateProfileResponse getMyProfile(User currentUser);

    CandidateProfile getMyProfileEntity(User currentUser);
}