package com.tek.jpa.service.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.domain.Store;
import com.tek.jpa.domain.Store.Id;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.repository.mock.StoreRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class StoreReadOnlyDalService extends ReadOnlyDalService<Store, Id> {

  protected StoreReadOnlyDalService(
      EntityManager entityManager,
      ObjectMapper objectMapper
  ) {
    super(entityManager, objectMapper);
  }

  @Override
  protected ReadOnlyDalRepository<Store, Id> repository() {
    return new ReadOnlyDalRepository<>(context.getBean(StoreRepository.class)) {
    };
  }
}
