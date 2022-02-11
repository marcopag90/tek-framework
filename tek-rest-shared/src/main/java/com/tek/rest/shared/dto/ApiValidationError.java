package com.tek.rest.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@SuppressWarnings("squid:S1948")
public class ApiValidationError implements Serializable {

  @JsonProperty("object")
  private String object;
  @JsonProperty("field")
  private String field;
  @JsonProperty("rejectedValue")
  private Object rejectedValue;
  @JsonProperty("message")
  private String message;

  public ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }

  public ApiValidationError(
      String object,
      String field,
      Object rejectedValue,
      String message
  ) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public Object getRejectedValue() {
    return rejectedValue;
  }

  public void setRejectedValue(Object rejectedValue) {
    this.rejectedValue = rejectedValue;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
