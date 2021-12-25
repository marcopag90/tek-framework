package com.tek.jpa.repository;

import java.io.Serializable;
import org.springframework.lang.NonNull;

/**
 * Repository for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class WritableDalRepository<E extends Serializable, I extends Serializable>
    extends ReadOnlyDalRepository<E, I> {

  protected WritableDalRepository(DalRepository<E, I> repository) {
    super(repository);
  }

  public E create(@NonNull E entity) {
    return repository.save(entity);
  }

  public E update(@NonNull E entity) {
    return repository.save(entity);
  }

  public void deleteById(@NonNull I id) {
    repository.deleteById(id);
  }

}
