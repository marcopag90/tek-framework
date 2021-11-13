package com.tek.jpa.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import java.time.LocalDate;
import java.util.UUID;
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
  private UUID id;

  private String name;

  private LocalDate birthDate;

  public static class Views {

    public static class UserView {

    }

    public static class DeveloperView {

    }
  }

}
