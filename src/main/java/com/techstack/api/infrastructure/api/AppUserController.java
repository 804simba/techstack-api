package com.techstack.api.infrastructure.api;

import com.techstack.api.commons.ApiRoute;
import com.techstack.api.infrastructure.service.AppUserService;
import com.techstack.api.payload.data.UserData;
import com.techstack.api.payload.request.user.CreateAppUserRequest;
import com.techstack.api.payload.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "App User", description = "App User API")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoute.AppUser.BASE)
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping(ApiRoute.AppUser.NEW)
    public ApiResponse<UserData> create(@RequestBody @Valid CreateAppUserRequest request) {
        return appUserService.create(request);
    }

    @GetMapping(ApiRoute.AppUser.ALL)
    public ApiResponse<List<UserData>> getAll(@RequestParam(defaultValue = "0") Integer pageNumber,
                                              @RequestParam(defaultValue = "20") Integer pageSize) {
        return appUserService.getAll(pageNumber, pageSize);
    }
}
