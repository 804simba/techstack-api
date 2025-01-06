package com.techstack.api.infrastructure.service;

import com.techstack.api.payload.data.AuthenticationData;
import com.techstack.api.payload.response.ApiResponse;

public interface AuthenticationService {
    ApiResponse<AuthenticationData> login(String email, String password);
}
