package com.tek.jpa.repository;

import java.io.Serializable;
import org.springframework.stereotype.Repository;

/**
 * Repository for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
@Repository
public abstract class WritableDalRepository<E extends Serializable, I extends Serializable>
    extends ReadOnlyDalRepository<E, I> {

  protected WritableDalRepository(DalRepository<E, I> repository) {
    super(repository);
  }

  public E create(E entity) {
    return repository.save(entity);
  }

  public E update(E entity) {
    return repository.save(entity);
  }

  public void deleteById(I id) {
    repository.deleteById(id);
  }

}
