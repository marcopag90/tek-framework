package com.tek.jpa.utils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * Utility to enhance the {@link Specification} class with some useful methods.
 *
 * @author MarcoPagan
 */
public class PredicateUtils {

  private PredicateUtils() {
  }

  public record ByIdSpecification<E>(
      EntityType<E> entityType,
      Serializable id
  ) implements Specification<E> {

    @SneakyThrows
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
          final var propertyDescriptor = new PropertyDescriptor(name, id.getClass());
          final var getter = propertyDescriptor.getReadMethod();
          final var value = getter.invoke(id);
          predicates.add(cb.equal(root.get(name), value));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
      }
    }
  }
}
