package com.tek.jpa.repository;

import java.io.Serializable;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * Repository for jpa-based entities, allowing read-only crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class ReadOnlyDalRepository<E extends Serializable, I extends Serializable> {

  protected final DalRepository<E, I> repository;

  protected ReadOnlyDalRepository(DalRepository<E, I> repository) {
    this.repository = repository;
  }

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());

  public Optional<E> findOne(@Nullable Specification<E> specification) {
    return repository.findOne(specification);
  }

  public Page<E> findAll(@Nullable Specification<E> specification, @NonNull Pageable pageable) {
    return repository.findAll(specification, pageable);
  }
}
