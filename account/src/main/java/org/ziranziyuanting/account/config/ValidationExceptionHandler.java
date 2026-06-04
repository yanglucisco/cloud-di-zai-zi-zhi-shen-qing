package org.ziranziyuanting.account.config;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ziranziyuanting.common.api.ApiResponse;
import org.ziranziyuanting.common.api.BusinessException;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ValidationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidationExceptionHandler.class);

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
     * 处理业务异常（如账号重复、数据不存在等已知业务规则冲突）
     * 将异常消息原样返回给前端，HTTP 状态码由 BusinessException 决定
     */
    @SuppressWarnings("null")
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.getHttpStatus());
        if (status == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        ApiResponse<Void> response = ApiResponse.error(
                status.value(),
                ex.getMessage());
        return Mono.just(ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response));
    }

    /**
     * 兜底：处理所有未捕获的异常，返回中文错误信息
     */
    @SuppressWarnings("null")
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleException(Exception ex) {
        log.error("未捕获的异常", ex);
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "服务器内部错误");
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response));
    }
}
