package com.tek.rest.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import org.springframework.lang.Nullable;

public class UpdateRequest implements Serializable {

  @JsonProperty("properties")
  private Map<String, Serializable> properties = Map.of();
  @JsonProperty("version")
  private Serializable version;

  public Map<String, Serializable> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Serializable> properties) {
    this.properties = properties;
  }

  @Nullable
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
