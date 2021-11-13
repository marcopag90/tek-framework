package com.tek.mongo.controller;

import com.tek.mongo.repository.TekMongoRepository;
import com.tek.rest.shared.exception.EntityNotFoundException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class TekReadOnlyMongoController<T, I> implements TekReadOnlyMongoApi<T, I> {

  @Autowired
  protected TekMongoRepository<T, I> repository;

  @Override
  public Page<T> findAll(Document bson, Pageable pageable) {
    return repository.findAll(bson, pageable);
  }

  @Override
  public T findById(I id) {
    return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.class, id));
  }
}
