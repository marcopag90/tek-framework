package com.tek.jpa.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TekJpaRepository<T, I> extends CrudRepository<T, I>, JpaSpecificationExecutor<T> {

  @Override
  Page<T> findAll(Specification<T> specification, Pageable pageable);

  @Override
  Optional<T> findById(I i);
}
