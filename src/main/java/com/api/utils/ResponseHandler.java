package com.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ResponseHandler {

    private static final Logger LOGGER = LogManager.getLogger(ResponseHandler.class);

    public static <T> T deserializedResponse(Response response, Class<T> clazz ) {
        ObjectMapper objectMapper = new ObjectMapper();
        T responseDeserialized = null;

        try {
            responseDeserialized = objectMapper.readValue(response.asString(), clazz);
            String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDeserialized);
            LOGGER.info("Handling Response: \n" + jsonStr);
        } catch (JsonProcessingException jsonProcessingException) {
            LOGGER.error("JSON processing error: ");
        } catch (Exception exception) {
            LOGGER.error("Exception during deserialization: ");
        }

        return responseDeserialized;
    }

}
