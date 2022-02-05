package com.tek.rest.shared.dto;

import java.io.Serializable;
import java.util.Map;
import org.springframework.lang.Nullable;

public class UpdateRequest implements Serializable {

  private Map<String, Serializable> properties = Map.of();

  @Nullable
  private Serializable version;

  public Map<String, Serializable> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Serializable> properties) {
    this.properties = properties;
  }

  public Serializable getVersion() {
    return version;
  }

  public void setVersion(@Nullable Serializable version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "UpdateRequest{" +
        "properties=" + properties +
        ", version=" + version +
        '}';
  }
}
