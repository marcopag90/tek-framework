package com.tek.core.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;

/**
 * Italian Boolean Type Conversion.
 * <p>
 * This implementation considers anything different from character "S" including **null**
 * as false.
 *
 * @author MarcoPagan
 */
@Slf4j
public class ItalianBooleanConverter implements AttributeConverter<Boolean, String> {

    private static final String newLine = System.getProperty("line.separator");

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute != null) {
            if (attribute) {
                return "S";
            } else {
                return "N";
            }
        } else {
            return "N";
        }
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData != null && dbData.length() > 1) {
            String errorMessage = String.join("", newLine)
                .concat("Data retrieved contains at least 1 blank char. ")
                .concat("Ignoring blank chars to avoid equality failure. ")
                .concat("You should check your db column!");
            log.error(errorMessage);
            return "S".equals(dbData.trim());
        }
        return "S".equals(dbData);
    }
}
