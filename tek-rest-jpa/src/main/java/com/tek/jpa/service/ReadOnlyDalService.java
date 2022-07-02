package com.tek.jpa.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tek.jpa.repository.JpaDalRepository;
import com.tek.jpa.utils.JpaDalEntity;
import com.tek.jpa.utils.PredicateUtils.ByIdSpecification;
import com.tek.rest.shared.dto.ApiPage;
import com.tek.rest.shared.exception.DalConfigurationException;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.support.Repositories;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * Abstract Data Access Layer for jpa-based entities, allowing read operations.
 * <p> This layer sits in front of the repository layer and acts as a bridge to access the
 * repository data: it defines and control what can be read and takes care of manipulating
 * input/output data, according to the business logic provided by the developer.
 * <p> A minimal setup requires the following actions:
 * <ul>
 *   <li>
 *     <b>optionally</b> implement the <i>withJsonBuilder()</i> method to customize the behaviour
 *     of the {@link JsonMapper} used by the dal to serialize the entity;
 *   </li>
 *   <li>
 *     <b>optionally</b> implement the <i>where()</i> method to specify a sql <i>where</i> condition
 *     to be applied on every query;
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
 * public class AuthorDalService extends ReadOnlyDalService{@literal <}Author, Integer{@literal >} {
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
public abstract class ReadOnlyDalService<E extends Serializable, I extends Serializable>
    implements IReadOnlyDalService<E, I> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());
  protected final Class<E> entityClass;

  protected final JsonMapper jsonMapper;
  protected final JpaDalRepository<E, I> repository;
  protected final EntityManager entityManager;
  protected final JpaDalEntity<E> jpaDalEntity;
  protected final UnaryOperator<E> entityView;

  @Override
  public Class<E> getEntityClass() {
    return entityClass;
  }

  protected ReadOnlyDalService(
      @NonNull ApplicationContext context,
      @NonNull EntityManager entityManager
  ) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(entityManager);
    log.debug("Initializing {}", ClassUtils.getUserClass(this).getSimpleName());
    this.entityManager = entityManager;
    this.jpaDalEntity = new JpaDalEntity<>(entityManager, getEntityType());
    this.entityClass = jpaDalEntity.getJavaType();
    this.jsonMapper = initializeJsonMapper(withJsonBuilder());
    this.repository = resolve(context);
    this.entityView = entity -> {
      try {
        return jsonMapper.readerFor(entityClass).readValue(
            jsonMapper.writerWithView(applyView()).withoutRootName().writeValueAsBytes(entity),
            entityClass
        );
      } catch (IOException ex) {
        log.error("Error while applying function", ex);
        return null;
      }
    };
  }

  /**
   * Method to customize the {@link JsonMapper} of the dal entity, providing a builder that will be
   * appended to the default one.
   */
  @Nullable
  protected JsonMapper.Builder withJsonBuilder() {
    return null;
  }

  /**
   * Method to append a sql <i>where</i> condition on all the query methods executed on the dal
   * entity.
   */
  @Nullable
  protected Specification<E> where() {
    return null;
  }

  /**
   * Method to apply a {@link com.fasterxml.jackson.annotation.JsonView} to the dal entity.
   */
  @Nullable
  protected Class<?> applyView() {
    return null;
  }

  @Override
  public ApiPage<E> findAll(@Nullable Specification<E> specification, @NonNull Pageable pageable) {
    Objects.requireNonNull(pageable);
    final var where = specification != null ? specification.and(where()) : where();
    return new ApiPage<>(repository.findAll(where, pageable).map(entityView));
  }

  @Override
  public E findById(@NonNull I id) throws EntityNotFoundException {
    Specification<E> whereId = queryById(Objects.requireNonNull(id));
    if (where() != null) {
      whereId = whereId.and(where());
    }
    return entityView.apply(
        repository.findOne(whereId)
            .orElseThrow(() -> new EntityNotFoundException(entityClass, id))
    );
  }

  protected JsonMapper initializeJsonMapper(@Nullable JsonMapper.Builder jsonMapper) {
    final var builder = jsonMapper != null ? jsonMapper : JsonMapper.builder();
    return builder
        .addModule(new JavaTimeModule())
        .addModule(new Hibernate5Module())
        .addModule(new GuavaModule())
        .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .serializationInclusion(Include.NON_NULL)
        .build();
  }

  @SuppressWarnings("unchecked")
  protected EntityType<E> getEntityType() {
    var resolvableType = ResolvableType.forClass(getClass()).as(ReadOnlyDalService.class);
    return entityManager.getMetamodel().entity((Class<E>) resolvableType.getGeneric(0).resolve());
  }

  protected Specification<E> queryById(@NonNull I id) {
    return new ByIdSpecification<>(jpaDalEntity.getEntityType(), Objects.requireNonNull(id));
  }

  @SuppressWarnings("unchecked")
  protected JpaDalRepository<E, I> resolve(ApplicationContext context) {
    final var repositories = new Repositories(Objects.requireNonNull(context));
    final var javaType = jpaDalEntity.getJavaType();
    final var optional = repositories.getRepositoryFor(javaType);
    final var simpleName = javaType.getSimpleName();
    if (optional.isEmpty()) {
      throw new DalConfigurationException("Could not find a repository for class " + simpleName);
    }
    final var springRepository = optional.get();
    if (springRepository instanceof JpaDalRepository<?, ?> dalRepository) {
      return (JpaDalRepository<E, I>) dalRepository;
    } else {
      throw new DalConfigurationException(
          springRepository.getClass().getSimpleName() + " doesn't extend "
              + JpaDalRepository.class.getSimpleName()
      );
    }
  }
}
