package com.tek.jpa.repository.impl;

import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class DalRepositoryImpl<T, I> extends SimpleJpaRepository<T, I> {

  public DalRepositoryImpl(
      JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  public DalRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }

}
