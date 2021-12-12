package com.tek.jpa.service.impl;

import com.tek.jpa.service.WritableDal;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

public abstract class WritableDalService<E, I> extends ReadOnlyDalService<E, I>
    implements WritableDal<E, I> {

  @Autowired
  protected Validator validator;

  private Method createMethod;
  private SpringValidatorAdapter validatorAdapter;

  @PostConstruct
  @SneakyThrows
  private void writableDalServiceSetup() {
    createMethod = getClass().getMethod("create", Object.class);
    validatorAdapter = new SpringValidatorAdapter(validator);
  }

  @Override
  @SneakyThrows
  public E create(E entity) {
    final var authorizedEntity = select.apply(entity);
    var validation = new BeanPropertyBindingResult(null, getEntityType().getName());
    validatorAdapter.validate(authorizedEntity, validation);
    if (validation.hasErrors()) {
      throw new MethodArgumentNotValidException(new MethodParameter(createMethod, 0), validation);
    }
    return select.apply(dalRepository().save(authorizedEntity));
  }

  @Override
  public void deleteById(I id) {
    dalRepository().deleteById(id);
  }
}
