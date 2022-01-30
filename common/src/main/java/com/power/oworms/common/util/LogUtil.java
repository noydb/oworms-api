package com.power.oworms.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void log(String action, Object arg) {
        try {
            String json = MAPPER.writeValueAsString(arg);

            LOGGER.info("${}. Request: ${}", action, json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while converting object to json");
        }
    }

}
