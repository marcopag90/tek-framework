package com.tek.jpa.service;

import com.tek.jpa.domain.Beer;
import com.tek.jpa.repository.BeerRepository;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.service.impl.ReadOnlyDalService;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class BeerReadOnlyDalService extends ReadOnlyDalService<Beer, Long> {

  @Override
  public EntityManager entityManager() {
    return context.getBean(EntityManager.class);
  }

  @Override
  public DalRepository<Beer, Long> repository() {
    return context.getBean(BeerRepository.class);
  }

  @Override
  protected Class<?> selectFields() {
    return null;
  }
}
