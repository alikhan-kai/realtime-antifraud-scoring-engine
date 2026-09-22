package kz.kaspi.core.antifraudengine.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        log.error("Внутренняя ошибка сервера: ", ex);
        return ResponseEntity.internalServerError().body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
    }
}
