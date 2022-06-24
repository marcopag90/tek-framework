package com.tek.jpa.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repository interface for jpa-based entities.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
@NoRepositoryBean
public interface JpaDalRepository<E, I> extends JpaRepositoryImplementation<E, I> {

}
