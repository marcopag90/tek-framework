package com.tek.jpa.repository.mock;

import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorReadOnlyDalRepository extends ReadOnlyDalRepository<Author, Integer> {

  protected AuthorReadOnlyDalRepository(DalRepository<Author, Integer> repository) {
    super(repository);
  }
}
