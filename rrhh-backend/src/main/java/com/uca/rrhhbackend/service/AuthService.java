package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.LoginRequest;
import com.uca.rrhhbackend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}