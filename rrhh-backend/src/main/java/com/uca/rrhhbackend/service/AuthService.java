package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.LoginRequest;
import com.uca.rrhhbackend.dto.request.RegisterRequest;
import com.uca.rrhhbackend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}