package com.tek.jpa.repository.mock;

import com.tek.jpa.domain.Employee;
import com.tek.jpa.repository.JpaDalRepository;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

public interface EmployeeRepository extends JpaDalRepository<Employee, Long> {

  @Override
  @EntityGraph(value = "Employee.full")
  Optional<Employee> findOne(Specification<Employee> spec);
}
