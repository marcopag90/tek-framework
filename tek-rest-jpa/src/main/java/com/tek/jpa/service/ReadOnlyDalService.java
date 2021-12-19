package com.tek.jpa.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.utils.EntityManagerUtils;
import com.tek.rest.shared.exception.EntityNotFoundException;
import com.turkraft.springfilter.FilterBuilder;
import com.turkraft.springfilter.boot.FilterSpecification;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.UnaryOperator;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * Service for jpa-based entities, allowing read-only crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class ReadOnlyDalService<E extends Serializable, I extends Serializable> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());

  @Autowired
  protected ApplicationContext context;
  @Getter
  protected Class<E> entityClass;
  protected ObjectMapper objectMapper;
  protected EntityManagerUtils entityManagerUtils;
  private DalRepository<E, I> repository;

  protected abstract EntityManager entityManager();

  //TODO interface with implementation to restrict method access
  protected abstract DalRepository<E, I> dalRepository();

  @PostConstruct
  void setup() {
    entityClass = getEntityType().getJavaType();
    entityManagerUtils = new EntityManagerUtils(entityManager());
    repository = dalRepository();
    objectMapper = context.getBean(ObjectMapper.class).copy()
        .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
  }

  @Nullable
  protected Specification<E> where() {
    return null;
  }

  @Nullable
  protected Class<?> authorizedView() {
    return null;
  }

  public Page<E> findAll(Specification<E> specification, Pageable pageable) {
    if (where() != null) {
      specification.and(where());
    }
    return repository.findAll(specification, pageable).map(authorizeEntity);
  }

  /*
  TODO
     recuperare nome campo dell'id per creare la whereId
   */
  @SneakyThrows
  public E findById(I id) {
    final var whereId = new FilterSpecification<E>(FilterBuilder.equal("id", id));
    if (where() != null) {
      whereId.and(where());
    }
    return authorizeEntity.apply(
        repository.findOne(whereId).orElseThrow(() -> new EntityNotFoundException(entityClass, id))
    );
  }

  protected final UnaryOperator<E> authorizeEntity = entity -> {
    try {
      return objectMapper.readerFor(entityClass).readValue(
          objectMapper.writerWithView(authorizedView()).withoutRootName()
              .writeValueAsBytes(entity),
          entityClass
      );
    } catch (IOException ex) {
      log.error("Error while applying function", ex);
      return null;
    }
  };

  @SuppressWarnings("unchecked")
  public final EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(getClass()).as(ReadOnlyDalService.class);
    return entityManager().getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }
}
