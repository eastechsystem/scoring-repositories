package com.redcare.scoring.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    public static String asJson(final Object jsonObject) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("Could not convert object to json", e);
        }
    }

    public static <T> T fromJson(final String value,
                                 final Class<T> valueType) {
        try {
            final ObjectMapper jacksonObjectMapper = new ObjectMapper();
            jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return jacksonObjectMapper.readValue(value, valueType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
