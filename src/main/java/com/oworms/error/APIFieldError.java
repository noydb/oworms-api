package com.oworms.error;

import org.springframework.validation.FieldError;

public class APIFieldError {

    private String field;
    private String message;

    public APIFieldError(final FieldError error) {
        // e.g. word.pronunciation
        // because word is nested in WordRequestDTO,
        // we slice from after "word." onwards
        this.field = error.getField().substring(error.getField().indexOf(".") + 1);
        this.message = error.getDefaultMessage();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
