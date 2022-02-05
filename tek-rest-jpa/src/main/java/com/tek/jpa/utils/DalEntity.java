package com.tek.jpa.utils;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type.PersistenceType;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.metamodel.model.domain.PersistentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;

/**
 * Utility to work with {@link javax.persistence.Entity}
 *
 * @author MarcoPagan
 */
@SuppressWarnings({"squid:S3457", "squid:S2629"})
public class DalEntity<E extends Serializable> {

  private final Logger log = LoggerFactory.getLogger(DalEntity.class);

  private static final String PATH_TOKENIZER = ".";
  private Metamodel metamodel;
  private EntityType<E> entityType;
  private Class<E> javaType;

  public Metamodel getMetamodel() {
    return metamodel;
  }

  public EntityType<E> getEntityType() {
    return entityType;
  }

  public Class<E> getJavaType() {
    return javaType;
  }

  @SuppressWarnings("unused")
  private DalEntity() {
  }

  public DalEntity(
      @NonNull EntityManager entityManager,
      @NonNull EntityType<E> entityType
  ) {
    this.metamodel = entityManager.getMetamodel();
    this.entityType = entityType;
    this.javaType = entityType.getJavaType();
  }

  /**
   * Method to check if an entity path is valid.
   * <p>E.g:</p>
   * <pre class="code">
   * class Author {
   *   private Set{@literal <}Book{@literal >}books;
   *   private String name;
   * }
   *
   * class Book {
   *   private Author author;
   * }
   * </pre>
   * <p>The path <b>books.author.name</b> describes a path starting from the Author class and
   * the tokens to walk down this path are "books", "author", "name".
   *
   * @param entityPath: the entity path
   * @param viewClass:  the optional view to apply
   * @throws IllegalArgumentException if the path is invalid
   * @throws AccessDeniedException    if the path is not accessible for the current viewClass
   * @throws NoSuchFieldException     if a getter for the given path is not found
   */
  public void validatePath(
      @NonNull String entityPath,
      @Nullable Class<?> viewClass
  ) throws IllegalArgumentException, AccessDeniedException, NoSuchFieldException {
    validatePath(new StringTokenizer(entityPath, PATH_TOKENIZER), entityType, viewClass);
  }

  @SuppressWarnings({"unchecked", "squid:S3740"})
  private void validatePath(
      @NonNull StringTokenizer pathTokenizer,
      @NonNull ManagedType<?> managedType,
      @Nullable Class<?> viewClass
  ) throws IllegalArgumentException, AccessDeniedException, NoSuchFieldException {
    if (pathTokenizer.hasMoreTokens()) {
      final var attributePath = pathTokenizer.nextToken();
      final var attribute = managedType.getDeclaredAttribute(attributePath);
      validateView(managedType, viewClass, attributePath);
      if (attribute instanceof final PersistentAttribute persistentAttribute) {
        final var persistenceType = persistentAttribute.getValueGraphType().getPersistenceType();
        final var attributeClass = persistentAttribute.getValueGraphType().getJavaType();
        if (persistenceType.equals(PersistenceType.BASIC)) {
          return;
        }
        if (persistenceType.equals(PersistenceType.ENTITY)) {
          validatePath(pathTokenizer, metamodel.managedType(attributeClass), viewClass);
        } else {
          throw new NotImplementedException("Type not yet implemented " + persistenceType);
        }
      }
    }
  }

  private void validateView(
      @NonNull ManagedType<?> managedType,
      @Nullable Class<?> view,
      @NonNull String attributePath
  ) throws NoSuchFieldException, AccessDeniedException {
    final var jsonView = managedType.getJavaType()
        .getDeclaredField(attributePath)
        .getAnnotation(JsonView.class);
    if (jsonView != null) {
      final var anyMatch = Arrays.asList(jsonView.value()).contains(view);
      if (!anyMatch) {
        log.warn(System.lineSeparator() + """
                    Access denied on path [{}] of managed type [{}] with view [{}].
                    Allowed views on path: {}
                """,
            attributePath,
            managedType.getJavaType().getName(),
            view != null ? view.getName() : "null",
            jsonView.value()
        );
        throw new AccessDeniedException("Operation not allowed!");
      }
    }
  }
}
