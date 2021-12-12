package com.tek.jpa.service;

import com.tek.jpa.repository.DalRepository;
import com.tek.rest.shared.exception.EntityNotFoundException;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for jpa-based entities, allowing read-only crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public interface ReadOnlyDal<E, I> {

  EntityManager entityManager();

  DalRepository<E, I> repository();

  Page<E> findAll(Specification<E> specification, Pageable pageable);

  E findById(I id) throws EntityNotFoundException;

}
