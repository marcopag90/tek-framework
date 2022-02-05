package com.tek.jpa.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * Utility to enhance the {@link Specification} class with some useful methods.
 *
 * @author MarcoPagan
 */
public final class PredicateUtils {

  private static final Logger log = LoggerFactory.getLogger(PredicateUtils.class);

  private PredicateUtils() {
  }

  public record ByIdSpecification<E>(
      EntityType<E> entityType,
      Serializable id
  ) implements Specification<E> {

    @Override
    public Predicate toPredicate(
        @NonNull Root<E> root,
        @NonNull CriteriaQuery<?> query,
        @NonNull CriteriaBuilder cb) {
      if (entityType.hasSingleIdAttribute()) {
        final var idAttribute = entityType.getId(id.getClass());
        return cb.equal(root.get(idAttribute.getName()), id);
      } else {
        final var classes = entityType.getIdClassAttributes();
        final var names = classes.stream().map(Attribute::getName).toList();
        final var predicates = new ArrayList<Predicate>();
        for (String name : names) {
          try {
            final var propertyDescriptor = new PropertyDescriptor(name, id.getClass());
            final var getter = propertyDescriptor.getReadMethod();
            final var value = getter.invoke(id);
            predicates.add(cb.equal(root.get(name), value));
          } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage() != null ? e.getMessage() : "", e);
          }
        }
        return cb.and(predicates.toArray(new Predicate[0]));
      }
    }
  }
}
