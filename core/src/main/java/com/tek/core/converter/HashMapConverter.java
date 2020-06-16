package com.tek.core.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.HashMap;

/**
 * Hashmap String Type Conversion.
 * <p>
 * This implementation converts a String representation of a map in a database (like plain JSON objects)
 * to a more friendly user HashMap java type.
 *
 * @author MarcoPagan
 */
@Slf4j
public class HashMapConverter implements AttributeConverter<HashMap<String, Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashMap<String, Object> attribute) {
        String text = null;
        try {
            text = objectMapper.writeValueAsString(attribute);
        } catch (Exception ex) {
            log.error("Writing error: {}", attribute);
        }
        return text;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<String, Object> convertToEntityAttribute(String dbData) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(dbData, map.getClass());
        } catch (Exception ex) {
            log.error("Reading error: {}", dbData);
        }
        return map;
    }
}
