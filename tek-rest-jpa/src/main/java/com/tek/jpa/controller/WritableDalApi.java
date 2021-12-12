package com.tek.jpa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Interface to provide a <i>CRUD</i> JPA Rest API.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface WritableDalApi<E, I> extends ReadOnlyDalApi<E, I> {

  String CAN_CREATE = "this.createAuthorized()";
  String CAN_UPDATE = "this.updateAuthorized()";
  String CAN_DELETE = "this.deleteAuthorized()";

  boolean createAuthorized();

  boolean updateAuthorized();

  boolean deleteAuthorized();

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @PreAuthorize(CAN_CREATE)
  E create(@RequestBody E entity);

  @DeleteMapping("/{id}")
  @PreAuthorize(CAN_DELETE)
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  I deleteById(@PathVariable I id);
}
