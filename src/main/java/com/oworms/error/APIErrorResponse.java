package com.oworms.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oworms.util.Utils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class APIErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final OffsetDateTime timestamp = Utils.now();
    private HttpStatus status;
    private int statusCode;
    private String message;
    private String path;
    private String httpMethod;
    private String uuid; // if a conflict occurs, we return the word's id so we can nav to it on the ui

    private List<APIFieldError> fieldErrors;

    private APIErrorResponse() {
    }

    public APIErrorResponse(final HttpStatus status, final String message, final HttpServletRequest httpServletRequest) {
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.path = httpServletRequest.getRequestURL().toString();
        this.httpMethod = httpServletRequest.getMethod();
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<APIFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<APIFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
