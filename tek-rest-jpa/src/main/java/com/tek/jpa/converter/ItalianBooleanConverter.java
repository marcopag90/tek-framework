package com.tek.jpa.converter;

import javax.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

/**
 * Italian Boolean Type Conversion.
 *
 * @author MarcoPagan
 */
@Slf4j
public class ItalianBooleanConverter implements AttributeConverter<Boolean, String> {

  private static final String NEWLINE = System.getProperty("line.separator");

  @Override
  public String convertToDatabaseColumn(Boolean attribute) {
    if (attribute == null) {
      return null;
    }
    return (Boolean.TRUE.equals(attribute)) ? "S" : "N";
  }

  @Override
  @SuppressWarnings("squid:S2447")
  public Boolean convertToEntityAttribute(String dbData) {
    if (StringUtils.isNotBlank(dbData)) {
      if (dbData.length() > 1) {
        final var message = String.join("", NEWLINE)
            .concat("Data retrieved contains at least 1 blank char. ")
            .concat("Ignoring blank chars to avoid equality failure. ")
            .concat("You should check your database column!");
        log.warn(message);
        return "S".equals(dbData.trim());
      } else {
        return "S".equals(dbData);
      }
    }
    return null;
  }
}
