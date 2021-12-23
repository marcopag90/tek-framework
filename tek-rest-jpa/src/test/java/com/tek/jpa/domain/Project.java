package com.tek.jpa.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@IdClass(Project.ProjectId.class)
public class Project implements Serializable {

  @Id
  private String name;
  @Id
  private String type;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProjectId implements Serializable {

    private String name;
    private String type;
  }

}
