package com.techstack.api.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;

    private String code;

    private String message;

    private T body;

    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> ok(String message){
        return ApiResponse.<T>builder()
                .success(true)
                .code(String.valueOf(200))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> ok(String message, T body){
        return ApiResponse.<T>builder()
                .success(true)
                .code(String.valueOf(200))
                .message(message)
                .body(body)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> badRequest(String message){
        return ApiResponse.<T>builder()
                .success(false)
                .code(String.valueOf(400))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> notFound(String message){
        return ApiResponse.<T>builder()
                .success(false)
                .code(String.valueOf(404))
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
