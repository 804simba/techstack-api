package com.techstack.api.infrastructure.service.impl;

import com.techstack.api.domain.entity.Address;
import com.techstack.api.domain.entity.AppUser;
import com.techstack.api.domain.repository.AppUserRepository;
import com.techstack.api.infrastructure.service.AppUserService;
import com.techstack.api.payload.data.UserData;
import com.techstack.api.payload.request.user.CreateAppUserRequest;
import com.techstack.api.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    @Override
    public ApiResponse<UserData> create(CreateAppUserRequest request) {
        var existingUser = appUserRepository.findAppUserByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
        if (existingUser.isPresent()) {
            return ApiResponse.badRequest("User already exists");
        }

        var address = Address.builder()
                .street(request.getAddress().getStreet())
                .city(request.getAddress().getCity())
                .state(request.getAddress().getState())
                .country(request.getAddress().getCountry())
                .zipCode(request.getAddress().getZipCode())
                .primary(true)
                .build();

        Set<Address> addresses = new HashSet<>();
        addresses.add(address);

        var newUser = AppUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .addresses(addresses)
                .build();

        appUserRepository.save(newUser);

        return ApiResponse.ok("User created successfully");
    }

    @Override
    public ApiResponse<List<UserData>> getAll(Integer pageNumber, Integer pageSize) {
        var pageable = PageRequest.ofSize(pageSize).withPage(pageNumber);
        var results = appUserRepository.findAll(pageable);
        if (results.isEmpty()) {
            return ApiResponse.notFound("No users found");
        }
        var data = results.stream().map(appUser -> UserData.builder()
                .name(appUser.getName())
                .email(appUser.getEmail())
                .phoneNumber(appUser.getPhoneNumber())
                .build())
                .toList();
        return ApiResponse.ok("Users retrieved successfully", data);
    }
}
