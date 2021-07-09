package com.power.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class OWormExceptionHandler {

    @ExceptionHandler(OWormException.class)
    public ResponseEntity<APIError> handleOWormException(HttpServletRequest httpServletRequest,
                                                         OWormException ex) {
        HttpStatus status = getStatusCode(ex.getErrorType());

        APIError apiError = new APIError(LocalDateTime.now(), status, ex.getMessage());

        return ResponseEntity.status(status).body(apiError);
    }

    private HttpStatus getStatusCode(OWormExceptionType exceptionType) {
        switch (exceptionType) {
            case WORD_EXISTS:
                return HttpStatus.CONFLICT;
            case WORD_NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case INSUFFICIENT_RIGHTS:
                return HttpStatus.FORBIDDEN;
            case GENERAL_FAILURE:
            case CSV_READ_FAILURE:
            case CSV_WRITE_FAILURE:
            case EMAIL_SEND_FAILURE:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
