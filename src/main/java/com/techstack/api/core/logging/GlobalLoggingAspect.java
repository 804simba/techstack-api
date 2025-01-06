package com.techstack.api.core.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GlobalLoggingAspect {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final ObjectMapper objectMapper;

  @PostConstruct
  public void setup() {
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Pointcut("execution(* com.techstack.api.infrastructure.api..*(..))")
  public void provideValuesForLogging() {}

  @AfterReturning(pointcut = "provideValuesForLogging()", returning = "returnValue")
  public void logMethods(JoinPoint joinPoint, Object returnValue) {
    try {
      HttpServletRequest request =
              ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      logRequest(request, joinPoint);

      logResponse(returnValue);

    } catch (Exception e) {
      logger.error("Failed to log request/response: {}", e.getMessage());
    }
  }

  private void logRequest(HttpServletRequest request, JoinPoint joinPoint) {
    try {
      Map<String, Object> requestLog = new HashMap<>();
      requestLog.put("url", request.getRequestURL().toString());
      requestLog.put("method", request.getMethod());
      requestLog.put("host", request.getHeader("Host"));
      requestLog.put("ip_address", request.getHeader("X-FORWARDED-FOR") != null ?
              request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr());
      requestLog.put("endpoint", joinPoint.getSignature().getName());

      if (!request.getMethod().equals("GET") && joinPoint.getArgs().length > 0) {
        Object requestBody = joinPoint.getArgs()[0];
        try {
          String jsonBody = objectMapper.writeValueAsString(requestBody);
          JsonNode jsonNode = objectMapper.readTree(jsonBody);
          requestLog.put("payload", jsonNode);
        } catch (JsonProcessingException e) {
          requestLog.put("payload", "Failed to parse request payload: " + e.getMessage());
        }
      }

      logger.info("\n==================== REQUEST ====================\n{}",
              objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestLog));
    } catch (JsonProcessingException e) {
      logger.error("Failed to log request: {}", e.getMessage());
    }
  }

  private void logResponse(Object returnValue) {
    try {
      logger.info("\n==================== RESPONSE ====================\n{}",
              objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(returnValue));
    } catch (JsonProcessingException e) {
      logger.error("Failed to log response: {}", e.getMessage());
    }
  }

  @AfterThrowing(pointcut = "provideValuesForLogging()", throwing = "ex")
  public void logExceptions(JoinPoint joinPoint, Throwable ex) {
    try {
      HttpServletRequest request =
              ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

      logRequest(request, joinPoint);

      Map<String, Object> errorLog = new HashMap<>();
      errorLog.put("error", ex.getMessage());
      errorLog.put("cause", ex.getCause() != null ? ex.getCause().getMessage() : null);
      errorLog.put("stack_trace", ex.getStackTrace());

      logger.error("\n==================== ERROR ====================\n{}",
              objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorLog));
      logger.error("Exception: {}, {}", ex.getMessage(), ex.getStackTrace());
    } catch (Exception e) {
      logger.error("Failed to log exception: {}", e.getMessage());
    }
  }
}
