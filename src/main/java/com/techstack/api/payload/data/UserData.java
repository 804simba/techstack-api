package com.techstack.api.payload.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private String name;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
}
