package com.tek.jpa.service;

import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Interface for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface WritableDal<E, I> extends ReadOnlyDal<E, I> {

  E create(E entity) throws MethodArgumentNotValidException;

  void deleteById(I id);

}
