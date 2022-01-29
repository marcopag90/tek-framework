package com.tek.core.properties.i18n;

import io.micrometer.core.instrument.util.StringUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.reflect.FieldUtils;

public class TekLocaleTypeValidator
    implements ConstraintValidator<TekLocaleTypeConstraint, String> {

  private static final List<String> allowedValues;
  private static final String LINE_SEPARATOR = System.lineSeparator();

  static {
    allowedValues = Arrays.stream(FieldUtils.getAllFields(TekLocaleConstants.class))
        .map(Field::getName).toList();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    final var matches = allowedValues.stream().anyMatch(t -> t.equals(value));
    if (!matches) {
      String message = String.format(
          "%s Unsupported locale type%s Allowed values are %s",
          LINE_SEPARATOR,
          StringUtils.isBlank(value) ? "." : String.format(" [%s].", value),
          allowedValues
      );
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      return false;
    }
    return true;
  }

}
