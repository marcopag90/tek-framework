package com.tek.jpa.service.impl;

import com.tek.jpa.service.WritableDal;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.validation.Validator;
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
  private void writableDalServiceSetup() throws NoSuchMethodException {
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
    return select.apply(repository().save(authorizedEntity));
  }

  @Override
  public void deleteById(I id) {
    repository().deleteById(id);
  }
}
