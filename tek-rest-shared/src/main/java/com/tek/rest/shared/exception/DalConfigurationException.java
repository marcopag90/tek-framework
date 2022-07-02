package com.tek.rest.shared.exception;

public class DalConfigurationException extends RuntimeException {

  public DalConfigurationException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public DalConfigurationException(String message) {
    super(message);
  }

}
