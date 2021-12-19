package com.tek.jpa.utils;

import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type.PersistenceType;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.metamodel.model.domain.PersistentAttribute;
import org.springframework.lang.NonNull;

/**
 * Utility to enhance the {@link EntityManager} with some useful methods.
 *
 * @author MarcoPagan
 */
public class EntityManagerUtils {

  private static final String PATH_TOKENIZER = ".";
  private Metamodel metamodel;

  private EntityManagerUtils() {
  }

  public EntityManagerUtils(@NonNull EntityManager entityManager) {
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
   * <p>The path <b>books.id.name</b> describes a path starting from the Author class and
   * the tokens to walk down this path are "books", "id", "name".
   *
   * @param entityPath:  the entity path
   * @param managedType: the {@link ManagedType} of the entity
   * @throws IllegalArgumentException if the path is invalid
   */
  public void validatePath(
      @NonNull String entityPath,
      @NonNull ManagedType<?> managedType
  ) throws IllegalArgumentException {
    validatePath(new StringTokenizer(entityPath, PATH_TOKENIZER), managedType);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void validatePath(
      @NonNull StringTokenizer pathTokenizer,
      @NonNull ManagedType<?> managedType
  ) throws IllegalArgumentException {
    if (pathTokenizer.hasMoreTokens()) {
      final var attributePath = pathTokenizer.nextToken();
      final var attribute = managedType.getDeclaredAttribute(attributePath);
      if (attribute instanceof PersistentAttribute) {
        final var persistentAttribute = ((PersistentAttribute) attribute);
        final var persistenceType = persistentAttribute.getValueGraphType().getPersistenceType();
        final var attributeClass = persistentAttribute.getValueGraphType().getJavaType();
        if (persistenceType.equals(PersistenceType.BASIC)) {
          return;
        }
        if (persistenceType.equals(PersistenceType.ENTITY)) {
          final var attributeManagedType = metamodel.managedType(attributeClass);
          validatePath(pathTokenizer, attributeManagedType);
        } else {
          throw new NotImplementedException("Type not yet implemented " + persistenceType);
        }
      }
    }
  }
}
