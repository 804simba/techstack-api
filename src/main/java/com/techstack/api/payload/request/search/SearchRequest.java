package com.techstack.api.payload.request.search;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequest {
    @NotBlank(message = "Keyword is required")
    private String keyword;
}
