package com.tek.jpa.service;

import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Service interface for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface WritableDalService<E, I> extends ReadOnlyDalService<E, I> {

  E create(E entity) throws MethodArgumentNotValidException;

}
