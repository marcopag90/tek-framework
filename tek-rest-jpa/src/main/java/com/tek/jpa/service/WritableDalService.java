package com.tek.jpa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.repository.WritableDalRepository;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Service for jpa-based entities, allowing all crud operations.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     implement the method <i>repository()</i> to qualify the {@link WritableDalRepository} to use;
 *   </li>
 *   <li>
 *     <b>optionally</b> implement the <i>where()</i> method to specify a mandatory where condition
 *     to be applied on every search query;
 *   </li>
 *   <li>
 *     <b>optionally</b> implement the <i>applyView()</i> method to apply {@link com.fasterxml.jackson.annotation.JsonView}
 *     classes, allowing to change the visibility of properties inside the Dal entity.
 *     This operation acts as a projection over the annotated fields.
 *   </li>
 * </ul>
 * <p>E.g:</p>
 * <pre class="code">
 * {@literal @Service}
 * public class AuthorDalService extends WritableDalService{@literal <}Author, Integer{@literal >} {
 *
 *   {@literal @Override}
 *   public WritableDalRepository repository() {
 *     return context.getBean(AuthorRepository.class);
 *   }
 *
 *   {@literal @Override}
 *   public Specification{@literal <}Author{@literal >} where() {
 *     ...
 *   }
 *
 *   {@literal @Override}
 *   public Class{@literal <}?{@literal >} applyView() {
 *     var auth = SecurityContextHolder.getContext().getAuthentication();
 *     if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DEVELOPER"))) {
 *       return DeveloperView.class;
 *     }
 *     return UserView.class;
 *   }
 * }
 * </pre>
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @param <I> : the {@link javax.persistence.Id}
 * @author MarcoPagan
 */
public abstract class WritableDalService<E extends Serializable, I extends Serializable>
    extends ReadOnlyDalService<E, I> {

  protected final Method createMethod;
  protected final Method patchMethod;
  protected final SpringValidatorAdapter validatorAdapter;

  protected abstract WritableDalRepository<E, I> repository();

  protected WritableDalService(
      @NonNull EntityManager entityManager,
      @NonNull ObjectMapper objectMapper,
      @NonNull Validator validator
  ) throws NoSuchMethodException {
    super(entityManager, objectMapper);
    createMethod = getClass().getMethod("create", Serializable.class);
    patchMethod = getClass().getMethod("update", Serializable.class, Map.class, Serializable.class);
    validatorAdapter = new SpringValidatorAdapter(validator);
  }

  @SneakyThrows
  public E create(@NonNull E entity) {
    final var entityView = this.entityView.apply(entity);
    final var validation = new BeanPropertyBindingResult(null, getEntityType().getName());
    validatorAdapter.validate(entityView, validation);
    if (validation.hasErrors()) {
      throw new MethodArgumentNotValidException(new MethodParameter(createMethod, 0), validation);
    }
    final var savedEntity = repository().create(entityView);
    return this.entityView.apply(savedEntity);
  }

  //TODO tests
  @SneakyThrows
  public E update(
      @NonNull I id,
      @NonNull Map<String, Serializable> properties,
      @Nullable final Serializable version
  ) {
    final var entityType = getEntityType();
    for (String property : properties.keySet()) {
      entityUtils.validatePath(property, entityType, applyView());
    }
    SingularAttribute<? super E, ?> versionAttribute = null;
    if (entityType.hasVersionAttribute()) {
      if (version == null) {
        final var result = new BeanPropertyBindingResult(null, entityType.getName());
        result.addError(new FieldError(
            entityType.getName(),
            "version",
            null,
            false,
            null,
            null,
            "This entity requires a version to be updated"
        ));
        throw new MethodArgumentNotValidException(new MethodParameter(patchMethod, 2), result);
      }
      versionAttribute = getVersionAttribute(entityType);
    }
    final var entity = findById(id);
    final var wrapper = PropertyAccessorFactory.forBeanPropertyAccess(entity);
    final var result = new BeanPropertyBindingResult(entity, entity.getClass().getName());
    if (versionAttribute != null) {
      final var storedVersion = wrapper.getPropertyValue(versionAttribute.getName());
      if (storedVersion != null) {
        final var convertedVersion = wrapper.convertIfNecessary(version, storedVersion.getClass());
        if (!storedVersion.equals(convertedVersion)) {
          result.addError(new FieldError(
              entity.getClass().getName(),
              "version",
              convertedVersion,
              false,
              null,
              null,
              "The currently stored version (" + storedVersion + ") doesn't match!"
          ));
          throw new MethodArgumentNotValidException(new MethodParameter(patchMethod, 2), result);
        }
      }
    }
    wrapper.setAutoGrowNestedPaths(true);
    wrapper.setPropertyValues(properties);
    validatorAdapter.validate(entity, result);
    if (result.hasErrors()) {
      throw new MethodArgumentNotValidException(new MethodParameter(patchMethod, 1), result);
    }
    final var savedEntity = repository().update(entity);
    return entityView.apply(savedEntity);
  }

  public void deleteById(I id) {
    if (findById(id) != null) {
      repository().deleteById(id);
    }
  }

  @SuppressWarnings("squid:S1452")
  protected final SingularAttribute<? super E, ?> getVersionAttribute(EntityType<E> entityType) {
    return entityType.getSingularAttributes().stream()
        .filter(SingularAttribute::isVersion).findFirst()
        .orElse(null);
  }
}
