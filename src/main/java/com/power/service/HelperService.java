package com.power.service;

import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * General purpose service exposing reusable logic to other services.
 *
 * @author bp
 */
@Service
public class HelperService {

    @Value("${permission.key}")
    private String permissionKey;

    public void checkPermission(String permissionKeyArg) {
        if (!permissionKey.equals(permissionKeyArg)) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that.");
        }
    }
}
