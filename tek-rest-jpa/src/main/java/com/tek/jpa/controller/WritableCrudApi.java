package com.tek.jpa.controller;

import com.tek.rest.shared.api.WritableApi;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface to provide a <i>CRUD</i> JPA Rest API.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface WritableCrudApi<E, I> extends WritableApi {

  @PostMapping
  @PreAuthorize(CAN_CREATE)
  E create(@RequestBody E entity);

  @DeleteMapping("/{id}")
  @PreAuthorize(CAN_DELETE)
  I delete(@PathVariable I id);
}
