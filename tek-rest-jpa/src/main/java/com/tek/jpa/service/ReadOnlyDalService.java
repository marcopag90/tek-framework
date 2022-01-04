package com.tek.jpa.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tek.jpa.repository.ReadOnlyDalRepository;
import com.tek.jpa.utils.DalEntity;
import com.tek.jpa.utils.PredicateUtils.ByIdSpecification;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.UnaryOperator;
import javax.persistence.EntityManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
 *     implement the method <i>repository()</i> to qualify the {@link ReadOnlyDalRepository} to use;
 *   </li>
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
 *   public ReadOnlyDalRepository repository() {
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
public abstract class ReadOnlyDalService<E extends Serializable, I extends Serializable>
implements IReadOnlyDalService<E, I> {

  protected Logger log = LoggerFactory.getLogger(ClassUtils.getUserClass(this).getSimpleName());

  @Autowired
  protected ApplicationContext context;
  @Getter
  private final Class<E> entityClass;

  protected final JsonMapper jsonMapper;
  protected final DalEntity<E> dalEntity;

  public final EntityManager entityManager;
  public final UnaryOperator<E> entityView;

  protected abstract ReadOnlyDalRepository<E, I> repository();

  protected ReadOnlyDalService(@NonNull EntityManager entityManager) {
    log.debug("Initializing {}", ClassUtils.getUserClass(this).getSimpleName());
    this.entityManager = entityManager;
    this.dalEntity = new DalEntity<>(entityManager);
    this.entityClass = dalEntity.getJavaType();
    this.jsonMapper = initializeJsonMapper(withJsonBuilder());
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
  public Page<E> findAll(@Nullable Specification<E> specification, @NonNull Pageable pageable) {
    Specification<E> where;
    if (specification != null) {
      where = specification.and(where());
    } else {
      where = where();
    }
    return repository().findAll(where, pageable).map(entityView);
  }

  @Override
  public E findById(@NonNull I id) throws EntityNotFoundException {
    final var whereId = new ByIdSpecification<>(dalEntity.getEntityType(), id);
    if (where() != null) {
      whereId.and(where());
    }
    return entityView.apply(
        repository().findOne(whereId)
            .orElseThrow(() -> new EntityNotFoundException(entityClass, id))
    );
  }

  private JsonMapper initializeJsonMapper(@Nullable JsonMapper.Builder jsonMapper) {
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
}
