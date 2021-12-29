package com.tek.jpa.repository;

import java.io.Serializable;
import org.springframework.lang.NonNull;

/**
 * Abstract Repository Layer for jpa-based entities, allowing both read and write operations.
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
 * public class AuthorWritableRepository extends WritableDalRepository{@literal <}Author, Integer{@literal >} {
 *
 *     protected AuthorWritableRepository(
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
