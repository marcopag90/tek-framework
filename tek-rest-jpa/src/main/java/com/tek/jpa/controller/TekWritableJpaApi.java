package com.tek.jpa.controller;

import com.tek.rest.shared.TekWritableApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface TekWritableJpaApi<T, I> extends TekWritableApi {

  @PostMapping
  @PreAuthorize(CAN_CREATE)
  ResponseEntity<T> create(@RequestBody T entity);

  @DeleteMapping("/{id}")
  @PreAuthorize(CAN_DELETE)
  ResponseEntity<I> deleteOne(@PathVariable I id);
}
