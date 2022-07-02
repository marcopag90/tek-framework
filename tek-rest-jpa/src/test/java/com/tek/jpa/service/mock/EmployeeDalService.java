package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Employee;
import com.tek.jpa.domain.Employee.EmployeeViews.CompanyView;
import com.tek.jpa.domain.Employee.EmployeeViews.DeveloperView;
import com.tek.jpa.service.WritableDalService;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDalService extends WritableDalService<Employee, Long> {

  protected EmployeeDalService(
      ApplicationContext context,
      EntityManager entityManager,
      Validator validator
  ) {
    super(context, entityManager, validator);
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
