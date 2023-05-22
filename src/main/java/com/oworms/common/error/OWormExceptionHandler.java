package com.oworms.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class OWormExceptionHandler {

    @ExceptionHandler(OWormException.class)
    public ResponseEntity<APIError> handleOWormException(final HttpServletRequest httpServletRequest, final OWormException ex) {
        final String errorMsg = ex.getMessage();
        final HttpStatus status = getStatusCode(ex.getErrorType());
        final APIError apiError = new APIError(status, errorMsg);

        if (status == HttpStatus.CONFLICT) {
            int indexOfColon = errorMsg.indexOf(":");
            String uuid = errorMsg.substring(indexOfColon + 1);
            apiError.setUuid(uuid);
        }

        return ResponseEntity.status(status).body(apiError);
    }

    private HttpStatus getStatusCode(OWormExceptionType exceptionType) {
        switch (exceptionType) {
            case CONFLICT:
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

    class APIError {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private LocalDateTime timestamp;
        private HttpStatus status;
        private int statusCode;
        private String message;
        private String uuid; // if a conflict occurs, we return the word's id so we can nav to it on the ui

        public APIError(HttpStatus status, String message) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.statusCode = status.value();
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

}
