package com.power.oworms.common.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class OWormExceptionHandler {

    @ExceptionHandler(OWormException.class)
    public ResponseEntity<APIError> handleOWormException(HttpServletRequest httpServletRequest,
                                                         OWormException ex) {
        HttpStatus status = getStatusCode(ex.getErrorType());

        APIError apiError = new APIError(status, ex.getMessage());

        return ResponseEntity.status(status).body(apiError);
    }

    private HttpStatus getStatusCode(OWormExceptionType exceptionType) {
        switch (exceptionType) {
            case WORD_EXISTS:
                return HttpStatus.CONFLICT;
            case NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case INSUFFICIENT_RIGHTS:
                return HttpStatus.FORBIDDEN;
            case REQUEST_LIMIT_EXCEEDED:
                return HttpStatus.TOO_MANY_REQUESTS;
            case FAILURE:
            case CSV_READ_FAILURE:
            case CSV_WRITE_FAILURE:
            case EMAIL_SEND_FAILURE:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
