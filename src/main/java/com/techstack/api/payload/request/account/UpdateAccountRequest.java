package com.techstack.api.payload.request.account;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAccountRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private Boolean activate = false;
}
