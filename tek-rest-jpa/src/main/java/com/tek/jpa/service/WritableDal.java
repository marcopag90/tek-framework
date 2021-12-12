package com.tek.jpa.service;

/**
 * Interface for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface WritableDal<E, I> extends ReadOnlyDal<E, I> {

  E create(E entity);

  void deleteById(I id);

}
