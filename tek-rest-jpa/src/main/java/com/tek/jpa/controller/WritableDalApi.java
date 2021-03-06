package com.tek.jpa.controller;

import com.tek.rest.shared.dto.UpdateRequest;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public interface WritableDalApi<E extends Serializable, I extends Serializable>
    extends ReadOnlyDalApi<E, I> {

  boolean createAuthorized();

  boolean updateAuthorized();

  boolean deleteAuthorized();

  // https://corvallis.udemy.com/course/spring-professional-certification-exam-tutorial-module-07/learn/lecture/18949560#notes min 6.20 per il created
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("this.createAuthorized()")
  E create(@RequestBody E entity) throws MethodArgumentNotValidException;

  @PatchMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{id}"
  )
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("this.updateAuthorized()")
  E update(@PathVariable I id, @RequestBody UpdateRequest request)
      throws EntityNotFoundException, NoSuchFieldException, MethodArgumentNotValidException;

  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  @PreAuthorize("this.deleteAuthorized()")
  void deleteById(@PathVariable I id) throws EntityNotFoundException;
}
