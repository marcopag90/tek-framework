package com.tek.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TekJpaRepository<T, I> extends CrudRepository<T, I>, JpaSpecificationExecutor<T> {

}
