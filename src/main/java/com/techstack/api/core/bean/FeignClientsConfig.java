package com.techstack.api.core.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techstack.api.infrastructure.integration.AuthenticationProperties;
import com.techstack.api.infrastructure.integration.AuthenticationApi;
import feign.Feign;
import feign.Logger;
import feign.RequestTemplate;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class FeignClientsConfig {
    private final ObjectMapper mapper;
    private final AuthenticationProperties authenticationProperties;

    @Bean
    public AuthenticationApi authenticationApi() {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger(AuthenticationApi.class))
            .logLevel(Logger.Level.FULL)
                .requestInterceptors(List.of(new AuthenticationInterceptor()))
            .target(AuthenticationApi.class, authenticationProperties.getUrl());
    }

    public static class AuthenticationInterceptor implements feign.RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            template.header("Content-Type", "application/json");
        }
    }
}