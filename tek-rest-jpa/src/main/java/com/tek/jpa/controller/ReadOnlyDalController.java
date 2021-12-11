package com.tek.jpa.controller;

import static com.tek.rest.shared.constants.TekRestSharedConstants.FILTER_NAME;

import com.tek.jpa.service.impl.BaseReadOnlyDalService;
import com.tek.rest.shared.api.ReadOnlyApi;
import com.tek.rest.shared.exception.EntityNotFoundException;
import com.tek.rest.shared.swagger.ApiPageable;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface to provide a read-only <i>CRUD</i> JPA Rest API.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface ReadOnlyDalController<E, I> extends ReadOnlyApi {

  BaseReadOnlyDalService<E, I> getService();

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CAN_READ)
  @ApiPageable
  Page<E> findAll(@Filter(parameterName = FILTER_NAME) Specification<E> spec, Pageable page);

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CAN_READ)
  E findById(@PathVariable("id") I id) throws EntityNotFoundException;


}
