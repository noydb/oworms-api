package com.oworms.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidationExceptionHandler {

    @Value("${app.version}")
    private String appVersion;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(final HttpServletRequest httpServletRequest,
                                                                      final MethodArgumentNotValidException ex) {
        final List<APIFieldError> fieldErrors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(APIFieldError::new)
                .collect(Collectors.toList());
        final String message = "Bad values supplied for: " + fieldErrors
                .stream()
                .map(APIFieldError::getField)
                .collect(Collectors.joining(", "));

        final APIErrorResponse errorResponse = new APIErrorResponse(
                appVersion,
                HttpStatus.BAD_REQUEST,
                message,
                httpServletRequest,
                fieldErrors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

}
