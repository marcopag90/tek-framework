package com.tek.jpa.controller;

import static com.tek.rest.shared.constants.TekRestConstants.FILTER_NAME;

import com.tek.rest.shared.TekReadOnlyApi;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface to provide a read-only <i>CRUD</i> JPA Rest API.
 *
 * @param <T> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 *
 * @author MarcoPagan
 */
public interface TekReadOnlyJpaApi<T, I> extends TekReadOnlyApi {

  @GetMapping
  @PreAuthorize(CAN_READ)
  Page<T> findAll(@Filter(parameterName = FILTER_NAME) Specification<T> spec, Pageable page);

  @GetMapping("/{id}")
  @PreAuthorize(CAN_READ)
  ResponseEntity<T> findById(@PathVariable("id") I id);
}
