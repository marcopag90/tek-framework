package com.tek.jpa.service;

import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Service for jpa-based entities, allowing all crud operations.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class WritableDalService<E extends Serializable, I extends Serializable>
    extends ReadOnlyDalService<E, I> {

  @Autowired
  protected Validator validator;

  private Method createMethod;
  private Method updateMethod;
  private SpringValidatorAdapter validatorAdapter;

  @Override
  @PostConstruct
  @SneakyThrows
  void setup() {
    super.setup();
    createMethod = getClass().getMethod("create", Serializable.class);
    updateMethod = getClass().getMethod("update", Serializable.class, Map.class);
    validatorAdapter = new SpringValidatorAdapter(validator);
  }

  @SneakyThrows
  public E create(E entity) {
    final var authorizedEntity = authorizeEntity.apply(entity);
    final var validation = new BeanPropertyBindingResult(null, getEntityType().getName());
    validatorAdapter.validate(authorizedEntity, validation);
    if (validation.hasErrors()) {
      throw new MethodArgumentNotValidException(new MethodParameter(createMethod, 0), validation);
    }
    final var createdEntity = dalRepository().save(authorizedEntity);
    return authorizeEntity.apply(createdEntity);
  }

  @SneakyThrows
  public E update(I id, Map<String, Serializable> properties) {
    final var repositoryEntity = dalRepository().findById(id)
        .orElseThrow(() -> new EntityNotFoundException(entityClass, id));
    properties.keySet().forEach(k -> entityManagerUtils.validatePath(k, getEntityType()));
    if (authorizedView() != null) {

    }
    return null;
  }

  public void deleteById(I id) {
    dalRepository().deleteById(id);
  }
}
