package com.oworms.common.error;

public class OWormException extends RuntimeException {

    private final OWormExceptionType errorType;
    private final String message;
    private final String debugMessage;

    public OWormException(OWormExceptionType errorType, String message, String debugMessage) {
        this.errorType = errorType;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public OWormException(OWormExceptionType errorType, String message) {
        this.errorType = errorType;
        this.message = message;
        this.debugMessage = null;
    }

    public OWormExceptionType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
