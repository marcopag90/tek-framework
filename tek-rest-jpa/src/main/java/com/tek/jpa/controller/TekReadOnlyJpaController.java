package com.tek.jpa.controller;

import com.tek.jpa.repository.TekJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

public class TekReadOnlyJpaController<T, I> implements TekReadOnlyJpaApi<T, I> {

  @Autowired
  protected TekJpaRepository<T, I> repository;

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return repository.findAll(spec, pageable);
  }

  @Override
  public ResponseEntity<T> findById(I id) {
    return repository.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
