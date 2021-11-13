package com.tek.jpa.service;

import com.tek.jpa.domain.Author;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorReadOnlyService extends TekJpaReadOnlyService<Author, UUID> {

  @Autowired
  private EntityManager manager;

  @Override
  protected EntityManager entityManager() {
    return manager;
  }

  @Override
  protected Class<?> selectFields() {
    return Author.Views.UserView.class;
  }

}
