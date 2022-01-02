package com.tek.jpa.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tek.jpa.domain.Company.CompanyViews.EmployeeView;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@ToString(exclude = "employees")
@EqualsAndHashCode(exclude = "employees")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  private String name;

  @OneToMany(mappedBy = "company")
  @JsonView(EmployeeView.class)
  private Set<Employee> employees = Set.of();

  public static class CompanyViews {

    public static class EmployeeView {

    }
  }

}
