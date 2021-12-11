package com.tek.jpa.service;

/**
 * Service interface for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface WritableDalService<E, I> extends ReadOnlyDalService<E, I> {


}
