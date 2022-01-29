package com.tek.rest.shared.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
public class UpdateRequest implements Serializable {

  private Map<String, Serializable> properties = Map.of();

  @Nullable
  private Serializable version;

}
