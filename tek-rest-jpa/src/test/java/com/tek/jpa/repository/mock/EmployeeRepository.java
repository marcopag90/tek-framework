package com.tek.jpa.repository.mock;

import com.tek.jpa.domain.Employee;
import com.tek.jpa.repository.DalRepository;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

public interface EmployeeRepository extends DalRepository<Employee, Long> {

  @Override
  @EntityGraph(value = "Employee.full")
  Optional<Employee> findOne(Specification<Employee> spec);
}
