package com.techstack.api.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private boolean success;
    private int code;
    private String message;
    private LocalDateTime timestamp;
}
