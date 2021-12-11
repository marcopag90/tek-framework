package com.tek.jpa.service.impl;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.DalRepository;
import com.tek.jpa.service.WritableDalService;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.UnaryOperator;
import javax.annotation.PostConstruct;
import javax.persistence.metamodel.EntityType;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

public abstract class BaseWritableDalService<E, I> implements WritableDalService<E, I> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());

  @Autowired protected ApplicationContext context;
  @Autowired protected Validator validator;

  protected Class<E> entityClass;
  protected ObjectMapper objectMapper;

  private SpringValidatorAdapter validatorAdapter;
  private Method createMethod;

  @Nullable
  protected abstract Class<?> selectFields();

  private DalRepository<E, I> repository;

  @PostConstruct
  private void setup() throws NoSuchMethodException {
    entityClass = getEntityType().getJavaType();
    repository = repository();
    objectMapper = context.getBean(ObjectMapper.class).copy()
        .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    createMethod = getClass().getMethod("create", Object.class);
    validatorAdapter = new SpringValidatorAdapter(validator);
  }

  @Override
  public E create(E entity) throws MethodArgumentNotValidException {
    final var authorizedEntity = select.apply(entity);
    var validation = new BeanPropertyBindingResult(null, getEntityType().getName());
    validatorAdapter.validate(authorizedEntity, validation);
    if (validation.hasErrors()) {
      throw new MethodArgumentNotValidException(new MethodParameter(createMethod, 0), validation);
    }
    final var savedEntity = repository.save(authorizedEntity);
    return select.apply(savedEntity);
  }

  @Override
  public Page<E> findAll(Specification<E> specification, Pageable pageable) {
    final var page = repository.findAll(specification, pageable);
    return page.map(select);
  }

  @Override
  public E findById(I id) throws EntityNotFoundException {
    final var entity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    return select.apply(entity);
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
  private EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(getClass()).as(BaseWritableDalService.class);
    return entityManager().getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }
}
