package com.techstack.api.infrastructure.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auth")
@Data
@Component
public class AuthenticationProperties {
    private String url;
}
