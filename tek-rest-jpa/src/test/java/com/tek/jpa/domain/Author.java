package com.tek.jpa.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"books", "ratings"})
@EqualsAndHashCode(exclude = {"books", "ratings"})
@NamedEntityGraph(
    name = "Authors.full",
    includeAllAttributes = true
)
public class Author {

  @Id
  @GeneratedValue
  @JsonView(DeveloperView.class)
  private Integer id;

  @NotBlank
  private String name;
  @NotBlank
  private String surname;
  private String pseudonym;
  private LocalDate birthDate;
  private LocalDate deathDate;
  private Integer absolute;

  @OneToMany(mappedBy = "author")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonManagedReference
  private Set<Book> books = Set.of();

  @ElementCollection
  private List<Integer> ratings = List.of();

  public static class Views {

    public static class UserView {

    }

    public static class DeveloperView {

    }
  }

}
