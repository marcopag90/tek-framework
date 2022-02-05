package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Employee;
import com.tek.jpa.domain.Employee.EmployeeViews.CompanyView;
import com.tek.jpa.domain.Employee.EmployeeViews.DeveloperView;
import com.tek.jpa.repository.WritableDalRepository;
import com.tek.jpa.repository.mock.EmployeeRepository;
import com.tek.jpa.service.WritableDalService;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDalService extends WritableDalService<Employee, Long> {

  protected EmployeeDalService(
      EntityManager entityManager,
      Validator validator
  ) throws NoSuchMethodException {
    super(entityManager, validator);
  }

  @Override
  public WritableDalRepository<Employee, Long> repository() {
    return new WritableDalRepository<>(context.getBean(EmployeeRepository.class)) {
    };
  }

  @Override
  protected Class<?> applyView() {
    final var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DEVELOPER"))) {
      return DeveloperView.class;
    }
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("COMPANY"))) {
      return CompanyView.class;
    }
    return null;
  }
}
