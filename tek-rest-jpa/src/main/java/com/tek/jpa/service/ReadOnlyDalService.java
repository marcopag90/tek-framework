package com.tek.jpa.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.DalRepository;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.IOException;
import java.util.function.UnaryOperator;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public abstract class ReadOnlyDalService<E, I> {

  @Autowired
  protected ApplicationContext context;

  protected EntityType<E> entityType;
  protected Class<E> entityClass;
  protected DalRepository<E, I> repository;
  protected ObjectMapper dalObjectMapper;

  protected abstract EntityManager entityManager();

  protected abstract DalRepository<E, I> dalRepository();

  protected abstract Class<?> selectFields();

  @PostConstruct
  protected void setup() {
    this.entityType = getEntityType();
    this.entityClass = getEntityType().getJavaType();
    this.repository = dalRepository();
    this.dalObjectMapper = context.getBean(ObjectMapper.class).copy()
        .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
  }

  public Page<E> findAll(Specification<E> specification, Pageable pageable) {
    final var page = repository.findAll(specification, pageable);
    return page.map(select);
  }

  public E findById(I id) {
    var entity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    return select.apply(entity);
  }

  protected final UnaryOperator<E> select = entity -> {
    try {
      return dalObjectMapper.readValue(
          dalObjectMapper.writerWithView(selectFields()).withoutRootName()
              .writeValueAsBytes(entity),
          entityClass
      );
    } catch (IOException ex) {
      return null;
    }
  };

  @SuppressWarnings("unchecked")
  private EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(this.getClass()).as(ReadOnlyDalService.class);
    return entityManager().getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }

}
