package com.power.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SecurityService {

    private List<String> getKeeganIDs() {
        return Collections.singletonList("keegan");
    }

    private List<String> getBPIDs() {
        return Arrays.asList("bp", "bpower", "benj", "banjo");
    }

    // should only be called after unknownUser check.
    public String getFormalUsername(String u) {
        return getBPIDs().contains(u) ? "bp" : "keegan";
    }

    public boolean unknownUser(String u) {
        return !getBPIDs().contains(u) && !getKeeganIDs().contains(u);
    }

    public boolean isBP(String arg) {
        if (arg == null) {
            return false;
        }

        return getBPIDs().stream().filter((u) -> u.contains(arg)).count() == 0;
    }

    public boolean isKeegan(String arg) {
        if (arg == null) {
            return false;
        }

        return getKeeganIDs().stream().filter((u) -> u.contains(arg)).count() == 0;
    }
}
