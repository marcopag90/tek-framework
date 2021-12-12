package com.tek.jpa.service.impl;


import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.service.ReadOnlyDalService;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.IOException;
import java.util.function.UnaryOperator;
import javax.annotation.PostConstruct;
import javax.persistence.metamodel.EntityType;
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

public abstract class BaseReadOnlyDalService<E, I> implements ReadOnlyDalService<E, I> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());

  @Autowired
  protected ApplicationContext context;
  protected Class<E> entityClass;
  protected ObjectMapper objectMapper;

  private DalRepository<E, I> repository;

  @Nullable
  protected abstract Class<?> selectFields();

  @PostConstruct
  private void readOnlyDalServiceSetup() {
    entityClass = getEntityType().getJavaType();
    repository = repository();
    objectMapper = context.getBean(ObjectMapper.class)
        .copy().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
  }

  @Override
  public Page<E> findAll(Specification<E> specification, Pageable pageable) {
    return repository.findAll(specification, pageable).map(select);
  }

  @Override
  public E findById(I id) throws EntityNotFoundException {
    return select.apply(
        repository.findById(id).orElseThrow(() -> new EntityNotFoundException(entityClass, id))
    );
  }

  protected final UnaryOperator<E> select = entity -> {
    try {
      return objectMapper.readValue(
          objectMapper.writerWithView(selectFields()).withoutRootName()
              .writeValueAsBytes(entity),
          entityClass
      );
    } catch (IOException ex) {
      return null;
    }
  };

  @SuppressWarnings("unchecked")
  protected final EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(getClass()).as(BaseReadOnlyDalService.class);
    return entityManager().getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }

}
