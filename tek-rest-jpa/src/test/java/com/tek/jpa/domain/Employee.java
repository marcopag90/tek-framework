package com.tek.jpa.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tek.jpa.domain.Employee.EmployeeViews.CompanyView;
import com.tek.jpa.domain.Employee.EmployeeViews.DeveloperView;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NamedEntityGraph(name = "Employee.full", includeAllAttributes = true)
public class Employee implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;
  private BigDecimal income;
  private LocalDate lastContract;

  @JsonView(DeveloperView.class)
  private Instant createdAt;

  @Version
  private Long optLock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonView(CompanyView.class)
  @ToString.Exclude
  private Company company;

  public static class EmployeeViews {

    public static class DeveloperView {

    }

    public static class CompanyView {

    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Employee employee = (Employee) o;
    return id != null && Objects.equals(id, employee.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
