package com.tek.jpa.repository.mock;

import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.repository.WritableDalRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorWritableDalRepository extends WritableDalRepository<Author, Integer> {

  protected AuthorWritableDalRepository(
      @Qualifier("AuthorRepository") DalRepository<Author, Integer> repository
  ) {
    super(repository);
  }

}
