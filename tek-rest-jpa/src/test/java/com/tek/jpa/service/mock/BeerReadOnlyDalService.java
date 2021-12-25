package com.tek.jpa.service.mock;

import com.tek.jpa.domain.Beer;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.repository.mock.BeerRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class BeerReadOnlyDalService extends ReadOnlyDalService<Beer, Long> {

  @Override
  public EntityManager entityManager() {
    return context.getBean(EntityManager.class);
  }

  @Override
  protected ReadOnlyDalRepository<Beer, Long> dalRepository() {
    return new ReadOnlyDalRepository<>(context.getBean(BeerRepository.class)) {
    };
  }
}
