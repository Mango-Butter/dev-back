package com.mangoboss.app.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModules(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static <T> T fromJson(final String json, final Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.JSON_PARSE_FAILED);
        }
    }

    public static String toJson(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.JSON_CONVERT_FAILED);
        }
    }

    public static <T> T fromJson(final String json, final TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.JSON_PARSE_FAILED);
        }
    }
}