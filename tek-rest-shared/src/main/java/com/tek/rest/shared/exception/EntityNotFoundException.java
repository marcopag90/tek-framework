package com.tek.rest.shared.exception;

import org.apache.commons.lang3.StringUtils;

public class EntityNotFoundException extends Exception {

  public EntityNotFoundException(Class<?> clazz, Object id) {
    super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), id));
  }

  private static String generateMessage(String entity, Object id) {
    return StringUtils.capitalize(entity) + " was not found for id: " + id;
  }
}
