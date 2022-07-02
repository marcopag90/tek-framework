package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import com.tek.jpa.domain.Author.Views.UserView;
import com.tek.jpa.service.WritableDalService;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorWritableDalService extends WritableDalService<Author, Integer> {


  protected AuthorWritableDalService(
      ApplicationContext context,
      EntityManager entityManager,
      Validator validator
  ) throws NoSuchMethodException {
    super(context, entityManager, validator);
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
