package com.tek.jpa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.TekJpaRepository;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.util.function.UnaryOperator;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public abstract class TekJpaReadOnlyService<E, I> {

  @Autowired protected TekJpaRepository<E, I> repository;
  @Autowired protected ObjectMapper objectMapper;

  protected EntityType<E> entityType;
  protected Class<E> entityClass;

  protected abstract EntityManager entityManager();

  protected abstract Class<?> selectFields();

  @PostConstruct
  void setup() {
    this.entityType = getEntityType();
    this.entityClass = getEntityType().getJavaType();
  }

  public Page<E> findAll(Specification<E> specification, Pageable pageable) {
    var page = repository.findAll(specification, pageable);
    page.map(select);
    return page;
  }

  public E findById(I id) {
    var entity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    return select.apply(entity);
  }

  protected final UnaryOperator<E> select = entity -> {
    try {
      return objectMapper.readValue(
          objectMapper.writerWithView(selectFields()).writeValueAsString(entity),
          entityClass
      );
    } catch (JsonProcessingException ex) {
      return null;
    }
  };

  @SuppressWarnings("unchecked")
  private EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(this.getClass()).as(TekJpaReadOnlyService.class);
    return entityManager().getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }

}
