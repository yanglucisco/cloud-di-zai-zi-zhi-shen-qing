package org.ziranziyuanting.rolemanage.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ValidationExceptionHandler {
    
    // private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 处理@RequestBody参数验证错误
     */
    @SuppressWarnings("null")
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s: %s", 
                    fieldError.getField(), 
                    fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        
        ApiResponse<Void> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(), 
            "参数验证失败",
            errors
        );
        
        return Mono.just(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response));
    }
    
    /**
     * 处理@RequestParam/@PathVariable参数验证错误
     */
    @SuppressWarnings("null")
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleConstraintViolationException(
            ConstraintViolationException ex, ServerWebExchange exchange) {
        
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> String.format("%s: %s", 
                    violation.getPropertyPath(), 
                    violation.getMessage()))
                .collect(Collectors.toList());
        
        ApiResponse<Void> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(), 
            "参数验证失败",
            errors
        );
        
        return Mono.just(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response));
    }
    
    /**
     * 处理JSON解析错误
     */
    @SuppressWarnings("null")
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleServerWebInputException(
            ServerWebInputException ex, ServerWebExchange exchange) {
        
        String message = "请求参数格式错误";
        if (ex.getRootCause() instanceof JsonProcessingException) {
            message = "JSON格式错误";
        } else if (ex.getReason() != null) {
            message = ex.getReason();
        }
        
        ApiResponse<Void> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(), 
            message
        );
        
        return Mono.just(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response));
    }
    
    /**
     * 统一响应格式
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;
        private List<String> errors;
        private long timestamp;
        
        public static <T> ApiResponse<T> success(T data) {
            return ApiResponse.<T>builder()
                    .code(200)
                    .message("success")
                    .data(data)
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
        
        public static <T> ApiResponse<T> error(int code, String message) {
            return ApiResponse.<T>builder()
                    .code(code)
                    .message(message)
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
        
        public static <T> ApiResponse<T> error(int code, String message, List<String> errors) {
            return ApiResponse.<T>builder()
                    .code(code)
                    .message(message)
                    .errors(errors)
                    .timestamp(System.currentTimeMillis())
                    .build();
        }
    }
}
