package com.tek.jpa.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Author {

  @Id
  @GeneratedValue
  @JsonView(DeveloperView.class)
  private Integer id;
  private String name;
  private String surname;
  private LocalDate birthDate;
  private LocalDate deathDate;

  public static class Views {

    public static class UserView {

    }

    public static class DeveloperView {

    }
  }

}
