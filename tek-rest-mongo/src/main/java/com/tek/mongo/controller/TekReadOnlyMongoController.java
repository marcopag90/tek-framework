package com.tek.mongo.controller;

import com.tek.mongo.repository.TekMongoRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class TekReadOnlyMongoController<T, I> implements TekReadOnlyMongoApi<T, I> {

  @Autowired
  protected TekMongoRepository<T, I> repository;

  @Override
  public Page<T> findAll(Document bson, Pageable pageable) {
    return repository.findAll(bson, pageable);
  }

  @Override
  public ResponseEntity<T> findById(I id) {
    return repository.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
