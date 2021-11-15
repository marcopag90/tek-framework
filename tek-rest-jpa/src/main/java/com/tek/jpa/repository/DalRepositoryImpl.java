package com.tek.jpa.repository;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class DalRepositoryImpl<T, I> extends SimpleJpaRepository<T, I> {

  public DalRepositoryImpl(
      JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  public DalRepositoryImpl(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return super.findAll(pageable);
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return super.findAll(spec, pageable);
  }

}
