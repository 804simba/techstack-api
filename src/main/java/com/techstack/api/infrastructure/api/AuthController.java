package com.techstack.api.infrastructure.api;

import com.techstack.api.commons.ApiRoute;
import com.techstack.api.infrastructure.service.AuthenticationService;
import com.techstack.api.payload.data.AuthenticationData;
import com.techstack.api.payload.request.auth.LoginRequest;
import com.techstack.api.payload.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication API")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoute.Auth.BASE)
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping(ApiRoute.Auth.LOGIN)
    public ApiResponse<AuthenticationData> login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request.getUsername(), request.getPassword());
    }
}
