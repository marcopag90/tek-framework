package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Store;
import com.tek.jpa.domain.Store.Id;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.repository.StoreRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class StoreReadOnlyDalService extends ReadOnlyDalService<Store, Id> {

  @Override
  protected EntityManager entityManager() {
    return context.getBean(EntityManager.class);
  }

  @Override
  protected ReadOnlyDalRepository<Store, Id> dalRepository() {
    return new ReadOnlyDalRepository<>(context.getBean(StoreRepository.class)) {
    };
  }
}
