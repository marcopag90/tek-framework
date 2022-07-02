package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Book;
import com.tek.jpa.service.WritableDalService;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BookWritableDalService extends WritableDalService<Book, Long> {

  protected BookWritableDalService(
      ApplicationContext context,
      EntityManager entityManager,
      Validator validator
  ) {
    super(context, entityManager, validator);
  }

}
