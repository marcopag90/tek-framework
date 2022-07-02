package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Beer;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BeerReadOnlyDalService extends ReadOnlyDalService<Beer, Long> {

  protected BeerReadOnlyDalService(
      ApplicationContext context,
      EntityManager entityManager
  ) {
    super(context, entityManager);
  }
}
