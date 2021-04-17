package com.tek.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ItalianBooleanConverterTest {

  private final ItalianBooleanConverter converter = new ItalianBooleanConverter();

  @Test
  void convertToDatabaseColumn() {
    Assertions.assertAll(
        () -> assertNull(converter.convertToDatabaseColumn(null)),
        () -> assertEquals("S", converter.convertToDatabaseColumn(true)),
        () -> assertEquals("N", converter.convertToDatabaseColumn(false))
    );
  }

  @Test
  void convertToEntityAttribute() {
    Assertions.assertAll(
        () -> assertNull(converter.convertToEntityAttribute(null)),
        () -> assertEquals(true, converter.convertToEntityAttribute("S")),
        () -> assertEquals(true, converter.convertToEntityAttribute("S ")),
        () -> assertEquals(false, converter.convertToEntityAttribute("N")),
        () -> assertEquals(false, converter.convertToEntityAttribute("N "))
    );
  }
}
