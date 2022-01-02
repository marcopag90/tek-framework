package com.tek.jpa.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tek.jpa.domain.Employee.EmployeeViews.CompanyView;
import com.tek.jpa.domain.Employee.EmployeeViews.DeveloperView;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@ToString(exclude = "company")
@EqualsAndHashCode(exclude = "company")
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
  private Company company;

  public static class EmployeeViews {

    public static class CompanyView {

    }

    public static class DeveloperView {

    }
  }

}
