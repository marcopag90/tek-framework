package com.tek.mongo.controller;

import com.tek.mongo.repository.TekMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public abstract class TekReadOnlyMongoController<T, I> implements TekReadOnlyMongoApi<T, I> {

  @Autowired
  protected TekMongoRepository<T, I> repository;

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
  public Page<T> findAll(Document bson, Pageable pageable) {
    return repository.findAll(bson, pageable);
  }

  @Override
  public ResponseEntity<T> findById(I id) {
    return repository.findById(id)
        .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
