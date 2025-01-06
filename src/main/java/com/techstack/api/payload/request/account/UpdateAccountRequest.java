package com.techstack.api.payload.request.account;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAccountRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private Boolean activate = false;
}
