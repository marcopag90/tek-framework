package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Store;
import com.tek.jpa.domain.Store.Id;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class StoreReadOnlyDalService extends ReadOnlyDalService<Store, Id> {

  protected StoreReadOnlyDalService(
      ApplicationContext context,
      EntityManager entityManager
  ) {
    super(context, entityManager);
  }
}
