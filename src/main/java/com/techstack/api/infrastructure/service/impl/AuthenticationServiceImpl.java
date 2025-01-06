package com.techstack.api.infrastructure.service.impl;

import com.techstack.api.infrastructure.integration.AuthenticationApi;
import com.techstack.api.core.exception.AppException;
import com.techstack.api.infrastructure.service.AuthenticationService;
import com.techstack.api.payload.data.AuthenticationData;
import com.techstack.api.payload.request.auth.LoginRequest;
import com.techstack.api.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationApi authenticationApi;
    @Override
    public ApiResponse<AuthenticationData> login(String email, String password) {
        try {
            var response = authenticationApi.login(LoginRequest.builder()
                    .username(email)
                    .password(password)
                    .build());
            return ApiResponse.ok("Login successful", response);
        } catch (Exception e) {
            throw new AppException("Login failed");
        }
    }
}