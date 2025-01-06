package com.techstack.api.infrastructure.service;

import com.techstack.api.payload.data.UserData;
import com.techstack.api.payload.request.user.CreateAppUserRequest;
import com.techstack.api.payload.response.ApiResponse;

import java.util.List;

public interface AppUserService {
    ApiResponse<UserData> create(CreateAppUserRequest request);

    ApiResponse<List<UserData>> getAll(Integer pageNumber, Integer pageSize);
}
