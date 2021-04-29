package com.tek.jpa.controller;

import static com.tek.rest.shared.constants.TekRestConstants.FILTER_NAME;

import com.tek.rest.shared.TekAuthApi;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface TekReadOnlyJpaApi<T, I> extends TekAuthApi {

  @Override
  default boolean createAuthorized() {
    return false;
  }

  @Override
  default boolean updateAuthorized() {
    return false;
  }

  @Override
  default boolean deleteAuthorized() {
    return false;
  }

  @GetMapping
  @PreAuthorize(CAN_READ)
  Page<T> findAll(@Filter(parameterName = FILTER_NAME) Specification<T> spec, Pageable page);

  @GetMapping("/{id}")
  @PreAuthorize(CAN_READ)
  ResponseEntity<T> findById(@PathVariable("id") I id);
}
