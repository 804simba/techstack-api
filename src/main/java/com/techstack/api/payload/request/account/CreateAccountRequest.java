package com.techstack.api.payload.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAccountRequest {
    @NotNull(message = "UserId is required")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "ProductId is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Name is required")
    @JsonProperty("name")
    private String name;
}
