package com.tek.jpa.utils;

import com.fasterxml.jackson.annotation.JsonView;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type.PersistenceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.metamodel.model.domain.PersistentAttribute;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Utility to work with {@link javax.persistence.Entity}
 *
 * @author MarcoPagan
 */
@Slf4j
public class EntityUtils {

  private static final String PATH_TOKENIZER = ".";
  private Metamodel metamodel;

  @SuppressWarnings("unused")
  private EntityUtils() {
  }

  public EntityUtils(@NonNull EntityManager entityManager) {
    this.metamodel = entityManager.getMetamodel();
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
   * @param entityPath:  the entity path
   * @param managedType: the {@link ManagedType} of the entity
   * @throws IllegalArgumentException if the path is invalid
   */
  public void validatePath(
      @NonNull String entityPath,
      @NonNull ManagedType<?> managedType,
      @Nullable Class<?> viewClass
  ) throws IllegalArgumentException, AccessDeniedException, NoSuchFieldException {
    validatePath(new StringTokenizer(entityPath, PATH_TOKENIZER), managedType, viewClass);
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
      if (viewClass != null) {
        validateView(managedType, viewClass, attributePath);
      }
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
      @NonNull Class<?> view,
      @NonNull String attributePath
  ) throws NoSuchFieldException, AccessDeniedException {
    final var jsonView = managedType.getJavaType()
        .getDeclaredField(attributePath)
        .getAnnotation(JsonView.class);
    if (jsonView != null) {
      final var declaredView = Arrays.stream(jsonView.value()).findFirst();
      if (declaredView.isPresent() && !declaredView.get().equals(view)) {
        log.warn(
            "Access denied while trying to access path {} of managed type {}",
            attributePath,
            managedType
        );
        throw new AccessDeniedException("Operation not allowed!");
      }
    }
  }
}
