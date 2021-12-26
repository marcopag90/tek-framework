package com.tek.jpa.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.utils.EntityUtils;
import com.tek.jpa.utils.PredicateUtils.ByIdSpecification;
import com.tek.rest.shared.exception.EntityNotFoundException;
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
import org.springframework.lang.NonNull;
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
  private Class<E> entityClass;

  protected final ObjectMapper objectMapper;
  protected EntityUtils entityUtils;
  protected ReadOnlyDalRepository<E, I> repository;

  public final EntityManager entityManager;
  public final UnaryOperator<E> entityView;

  protected abstract ReadOnlyDalRepository<E, I> repository();

  protected ReadOnlyDalService(
      @NonNull EntityManager entityManager,
      @NonNull ObjectMapper objectMapper
  ) {
    this.entityManager = entityManager;
    this.objectMapper = objectMapper;
    entityView = entity -> {
      try {
        return objectMapper.readerFor(entityClass).readValue(
            objectMapper.writerWithView(applyView()).withoutRootName().writeValueAsString(entity),
            entityClass
        );
      } catch (IOException ex) {
        log.error("Error while applying function", ex);
        return null;
      }
    };
  }

  @PostConstruct
  void setup() {
    this.repository = repository();
    this.entityClass = getEntityType().getJavaType();
    this.objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    this.entityUtils = new EntityUtils(entityManager);
  }

  @Nullable
  protected Specification<E> where() {
    return null;
  }

  @Nullable
  protected Class<?> applyView() {
    return null;
  }

  public Page<E> findAll(@Nullable Specification<E> specification, @NonNull Pageable pageable) {
    Specification<E> where;
    if (specification != null) {
      where = specification.and(where());
    } else {
      where = where();
    }
    return repository.findAll(where, pageable).map(entityView);
  }

  @SneakyThrows
  public E findById(@NonNull I id) {
    final var whereId = new ByIdSpecification<>(getEntityType(), id);
    if (where() != null) {
      whereId.and(where());
    }
    return entityView.apply(
        repository.findOne(whereId).orElseThrow(() -> new EntityNotFoundException(entityClass, id))
    );
  }

  @SuppressWarnings("unchecked")
  public final EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(getClass()).as(ReadOnlyDalService.class);
    return entityManager.getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }
}
