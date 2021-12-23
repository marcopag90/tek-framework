package com.tek.jpa.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

/**
 * Utility to enhance the {@link Specification} class with some useful methods.
 *
 * @author MarcoPagan
 */
public class PredicateUtils {

  private PredicateUtils() {
  }

  public static final class ByIdSpecification<E> implements Specification<E> {

    private final transient EntityType<E> entityType;
    private final Serializable id;

    public ByIdSpecification(EntityType<E> entityType, Serializable id) {
      this.entityType = entityType;
      this.id = id;
    }

    @SneakyThrows
    @SuppressWarnings("squid:S3011")
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
        final var names = classes.stream().map(Attribute::getName).collect(Collectors.toList());
        final var predicates = new ArrayList<Predicate>();
        for (String name : names) {
          final var field = ReflectionUtils.findField(id.getClass(), name);
          Objects.requireNonNull(field);
          field.setAccessible(true);
          final var value = (String) field.get(id);
          predicates.add(cb.equal(root.get(name), value));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
      }
    }
  }
}
