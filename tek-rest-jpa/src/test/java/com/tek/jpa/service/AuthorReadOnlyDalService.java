package com.tek.jpa.service;

import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Author.Views.DeveloperView;
import com.tek.jpa.domain.Author.Views.UserView;
import com.tek.jpa.repository.AuthorRepository;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.service.impl.ReadOnlyDalServiceImpl;
import javax.persistence.EntityManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorReadOnlyDalService extends ReadOnlyDalServiceImpl<Author, Integer> {

  @Override
  public DalRepository<Author, Integer> dalRepository() {
    return context.getBean(AuthorRepository.class);
  }

  @Override
  public EntityManager entityManager() {
    return context.getBean(EntityManager.class);
  }

  @Override
  protected Class<?> selectFields() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DEVELOPER"))) {
      return DeveloperView.class;
    }
    return UserView.class;
  }

}
