package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.entity.User;

import jakarta.servlet.http.HttpServletRequest;

public interface CurrentUserService {
    User getCurrentUser(HttpServletRequest request);

    void requireCandidate(User user);
}