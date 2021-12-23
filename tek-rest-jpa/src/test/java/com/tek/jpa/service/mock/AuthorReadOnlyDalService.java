package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import com.tek.jpa.domain.Author.Views.UserView;
import com.tek.jpa.repository.AuthorRepository;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorReadOnlyDalService extends ReadOnlyDalService<Author, Integer> {

  @Override
  protected ReadOnlyDalRepository<Author, Integer> dalRepository() {
    return new ReadOnlyDalRepository<>(context.getBean(AuthorRepository.class)) {
    };
  }

  @Override
  protected EntityManager entityManager() {
    return context.getBean(EntityManager.class);
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
