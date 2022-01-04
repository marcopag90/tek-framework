package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import com.tek.jpa.domain.Author.Views.UserView;
import com.tek.jpa.repository.WritableDalRepository;
import com.tek.jpa.repository.mock.AuthorRepository;
import com.tek.jpa.service.WritableDalService;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorWritableDalService extends WritableDalService<Author, Integer> {

  protected AuthorWritableDalService(
      EntityManager entityManager,
      Validator validator
  ) {
    super(entityManager, validator);
  }

  @Override
  public WritableDalRepository<Author, Integer> repository() {
    return new WritableDalRepository<>(context.getBean(AuthorRepository.class)) {
    };
  }

  @Override
  protected Class<?> applyView() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DEVELOPER"))) {
      return DeveloperView.class;
    }
    return UserView.class;
  }
}
