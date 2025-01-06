package com.techstack.api.infrastructure.integration;

import com.techstack.api.payload.data.AuthenticationData;
import com.techstack.api.payload.request.auth.LoginRequest;
import feign.RequestLine;

public interface AuthenticationApi {
    @RequestLine("POST /auth/login")
    AuthenticationData login(LoginRequest request);
}
