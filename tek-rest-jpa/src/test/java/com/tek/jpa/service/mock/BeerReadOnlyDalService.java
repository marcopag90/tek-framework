package com.tek.jpa.service.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.domain.Beer;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.repository.mock.BeerRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class BeerReadOnlyDalService extends ReadOnlyDalService<Beer, Long> {

  protected BeerReadOnlyDalService(
      EntityManager entityManager,
      ObjectMapper objectMapper
  ) {
    super(entityManager, objectMapper);
  }

  @Override
  protected ReadOnlyDalRepository<Beer, Long> repository() {
    return new ReadOnlyDalRepository<>(context.getBean(BeerRepository.class)) {
    };
  }
}
