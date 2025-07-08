package com.nisum.cartAndCheckout.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // --- USER AUTHENTICATION ---
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, "/login");
    }

    // --- ORDER RELATED ---
    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleOrderProcessing(OrderProcessingException ex) {
        log.warn("Order processing failed: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    // --- INVENTORY RELATED ---
    @ExceptionHandler({InventoryException.class, InventoryMappingException.class})
    public ResponseEntity<Map<String, Object>> handleInventoryErrors(RuntimeException ex) {
        log.warn("Inventory service error: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    // --- PROMO SERVICE ---
    @ExceptionHandler(PromoServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handlePromoService(PromoServiceUnavailableException ex) {
        log.warn("Promo service unavailable: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, null);
    }

    // --- FALLBACK HANDLER ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        log.error("Unhandled error occurred", ex);
        return buildErrorResponse("Something went wrong. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    // --- HELPER METHOD ---
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, String redirect) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        if (redirect != null) {
            error.put("redirect", redirect);
        }
        return new ResponseEntity<>(error, status);
    }
}
