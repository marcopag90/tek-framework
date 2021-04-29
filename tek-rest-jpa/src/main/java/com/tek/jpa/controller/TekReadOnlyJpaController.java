package com.tek.jpa.controller;

import com.tek.jpa.repository.TekJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public abstract class TekReadOnlyJpaController<T, I> implements TekReadOnlyJpaApi<T, I> {

  @Autowired
  protected TekJpaRepository<T, I> repository;

  @Override
  public final boolean createAuthorized() {
    return false;
  }

  @Override
  public boolean readAuthorized() {
    return isAuthorized();
  }

  @Override
  public final boolean updateAuthorized() {
    return false;
  }

  @Override
  public final boolean deleteAuthorized() {
    return false;
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return repository.findAll(spec, pageable);
  }

  @Override
  public ResponseEntity<T> findById(I id) {
    return repository.findById(id).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
