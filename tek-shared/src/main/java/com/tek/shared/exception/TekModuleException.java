package com.tek.shared.exception;

public final class TekModuleException extends Exception {

  public TekModuleException(String message) {
    super(message);
  }

  public TekModuleException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
