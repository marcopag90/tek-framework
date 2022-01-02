package com.tek.jpa.service.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.domain.Book;
import com.tek.jpa.repository.WritableDalRepository;
import com.tek.jpa.service.WritableDalService;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class BookWritableDalService extends WritableDalService<Book, Long> {

  protected BookWritableDalService(
      EntityManager entityManager,
      ObjectMapper objectMapper,
      Validator validator
  ) {
    super(entityManager, objectMapper, validator);
  }

  @Override
  protected WritableDalRepository<Book, Long> repository() {
    return null;
  }
}
