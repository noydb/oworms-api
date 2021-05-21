package com.power.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SettingsService {

    public List<String> getKeeganIDs() {
        return Arrays.asList("keegan");
    }

    public List<String> getBPIDs() {
        return Arrays.asList("bp", "bpower", "benj", "banjo");
    }
}
