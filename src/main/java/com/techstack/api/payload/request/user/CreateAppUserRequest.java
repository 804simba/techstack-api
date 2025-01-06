package com.techstack.api.payload.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techstack.api.payload.data.AddressData;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateAppUserRequest {
    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[A-Za-z]+(([',. -][A-Za-z ])?[A-Za-z]*)*$",
            message = "Name must contain only letters, spaces, and characters like . ' -"
    )
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("phone_number")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Address is required")
    private AddressData address;
}
