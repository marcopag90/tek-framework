package com.tek.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TekJpaRepository<T, I> extends JpaRepository<T, I>, JpaSpecificationExecutor<T> {

}
