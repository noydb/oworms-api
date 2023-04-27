package com.oworms.auth.error;

import javax.persistence.EntityExistsException;

public class AccountExistsException extends EntityExistsException {

    public AccountExistsException(String message) {
        super(message);
    }

}
