package com.oworms.common.error;

import com.oworms.common.util.Utils;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class OWormException extends RuntimeException {

    private final OWormExceptionType errorType;
    private final String message;
    private final String debugMessage;
    private final OffsetDateTime timestamp = OffsetDateTime.now(ZoneId.of(Utils.TIME_ZONE));

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
