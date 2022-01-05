package com.tek.jpa.repository.mock;

import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.DalRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Component;

@Component("AuthorRepository")
public interface AuthorRepository extends DalRepository<Author, Integer> {

  @Override
  @EntityGraph("Authors.full")
  Page<Author> findAll(Specification<Author> specification, @NonNull Pageable pageable);
}
