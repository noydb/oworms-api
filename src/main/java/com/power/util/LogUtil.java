package com.power.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.power.dto.WordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void log(String action, WordDTO wordDTO) {
        try {
            String json = objectMapper.writeValueAsString(wordDTO);

            logger.info("${}. Request: ${}", action, json);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting object to json");
        }
    }

    public static void log(String action) {
        logger.info(action);
    }
}
