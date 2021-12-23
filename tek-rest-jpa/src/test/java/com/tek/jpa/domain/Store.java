package com.tek.jpa.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Store implements Serializable {

  @EmbeddedId
  private Id id;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Embeddable
  public static class Id implements Serializable {

    private String name;
    private Integer code;
  }

}
