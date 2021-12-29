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
 * Abstract Repository layer for jpa-based entities, allowing read operations.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     declare the default constructor, to allow required parameter injection performed by Spring.
 *     <p>A {@link org.springframework.beans.factory.annotation.Qualifier} can be provided on the
 *     constructor, if required.
 *   </li>
 * </ul>
 * <p>E.g:</p>
 * <pre class="code">
 * {@literal @Repository}
 * public class AuthorReadOnlyRepository extends ReadOnlyDalRepository{@literal <}Author, Integer{@literal >} {
 *
 *     protected AuthorReadOnlyRepository(
 *       {@literal @Qualifier("AuthorRepo")} DalRepository{@literal <}Author, Integer{@literal > repository
 *     ) {
 *       super(repository);
 *     }
 *   }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class ReadOnlyDalRepository<E extends Serializable, I extends Serializable> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());
  protected final DalRepository<E, I> repository;

  protected ReadOnlyDalRepository(DalRepository<E, I> repository) {
    this.repository = repository;
  }

  public Optional<E> findOne(@Nullable Specification<E> specification) {
    return repository.findOne(specification);
  }

  public Page<E> findAll(@Nullable Specification<E> specification, @NonNull Pageable pageable) {
    return repository.findAll(specification, pageable);
  }
}
