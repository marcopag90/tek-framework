package com.tek.jpa.controller;

import org.springframework.http.ResponseEntity;

public class TekWritableJpaController<T, I>
    extends TekReadOnlyJpaController<T, I> implements TekWritableJpaApi<T, I> {

  //TODO see hibernate version for concurrent modification
  @Override
  public ResponseEntity<T> create(T entity) {
    return ResponseEntity.ok(repository.save(entity));
  }

  @Override
  public ResponseEntity<I> deleteOne(I id) {
    repository.deleteById(id);
    return ResponseEntity.ok(id);
  }
}
