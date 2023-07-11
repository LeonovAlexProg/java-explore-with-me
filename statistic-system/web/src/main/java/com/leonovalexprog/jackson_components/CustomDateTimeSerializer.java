package com.leonovalexprog.jackson_components;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class CustomDateTimeSerializer {
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private final String pattern = "yyyy-MM-dd HH:mm:ss";

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String datetime = jsonParser.getText();
            if (datetime.isEmpty() || datetime.isBlank()) {
                return null;
            }

            return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(pattern));
        }
    }
}
