package com.tek.core.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.NotSupportedException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;

/**
 * Implementation of {@link Specification} to create a {@link Predicate} to be executed inside
 * repositories extending {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 *
 * @author MarcoPagan
 */
@Getter
@Setter
@NoArgsConstructor
public class TekRsqlSpecification<T> implements Specification<T> {

  private String property;
  private transient ComparisonOperator operator;
  private List<String> arguments;

  private static final String NULL_ARG = "Argument cannot be null!";
  private static final String OPERATOR_NOT_SUPPORTED = "Operator not supported!";

  public TekRsqlSpecification(
      final String property,
      final ComparisonOperator operator,
      final List<String> arguments
  ) {
    super();
    this.property = property;
    this.operator = operator;
    this.arguments = arguments;
  }

  @SneakyThrows
  @SuppressWarnings("squid:S1199")
  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    List<Object> args = castArguments(root);
    val arg = args.get(0);
    val simpleOperator = TekRsqlSearchOperation.getSimpleOperator(operator);
    if (simpleOperator == null) {
      throw new NotSupportedException(operator.toString());
    }
    switch (simpleOperator) {
      case EQUAL:
        return equalPredicate(root, builder, arg);
      case NOT_EQUAL:
        return notEqualPredicate(root, builder, arg);
      case GREATER_THAN:
        return greaterThanPredicate(root, builder, arg);
      case GREATER_THAN_OR_EQUAL:
        return greaterThanOrEqualPredicate(root, builder, arg);
      case LESS_THAN:
        return lessThanPredicate(root, builder, arg);
      case LESS_THAN_OR_EQUAL:
        return lessThanOrEqualPredicate(root, builder, arg);
      case IN:
        return root.get(property).in(args);
      case NOT_IN:
        return builder.not(root.get(property).in(args));
      default:
        throw new UnsupportedOperationException(OPERATOR_NOT_SUPPORTED);
    }
  }

  private Predicate equalPredicate(Root<T> root, CriteriaBuilder builder, Object arg) {
    if (arg == null) {
      return builder.isNull(root.get(property));
    } else if (arg instanceof String) {
      if (((String) arg).contains("*")) {
        return builder.like(root.get(property), arg.toString().replace('*', '%'));
      } else {
        return builder.equal(root.get(property), arg.toString());
      }
    } else if (arg instanceof LocalDate) {
      return builder.equal(root.get(property), LocalDate.parse(arg.toString()));
    } else if (arg instanceof LocalDateTime) {
      return builder.equal(root.get(property), LocalDateTime.parse(arg.toString()));
    } else {
      return builder.equal(root.get(property), arg);
    }
  }

  private Predicate notEqualPredicate(Root<T> root, CriteriaBuilder builder, Object arg) {
    if (arg == null) {
      return builder.isNotNull(root.get(property));
    } else if (arg instanceof String) {
      if (((String) arg).contains("*")) {
        return builder.notLike(root.get(property), arg.toString().replace('*', '%'));
      } else {
        return builder.notEqual(root.get(property), arg);
      }
    } else if (arg instanceof LocalDate) {
      return builder.notEqual(root.get(property), LocalDate.parse(arg.toString()));
    } else if (arg instanceof LocalDateTime) {
      return builder.notEqual(root.get(property), LocalDateTime.parse(arg.toString()));
    } else if (arg instanceof Instant) {
      return builder.notEqual(root.get(property), Instant.parse(arg.toString()));
    } else {
      return builder.notEqual(root.get(property), arg);
    }
  }

  private Predicate greaterThanPredicate(Root<T> root, CriteriaBuilder builder, Object arg) {
    if (arg == null) {
      throw new UnsupportedOperationException(NULL_ARG);
    } else if (arg instanceof LocalDate) {
      return builder.greaterThan(root.get(property), LocalDate.parse(arg.toString()));
    } else if (arg instanceof LocalDateTime) {
      return builder.greaterThan(root.get(property), LocalDateTime.parse(arg.toString()));
    } else if (arg instanceof Instant) {
      return builder.greaterThan(root.get(property), Instant.parse(arg.toString()));
    } else {
      return builder.greaterThan(root.get(property), arg.toString());
    }
  }

  private Predicate greaterThanOrEqualPredicate(Root<T> root, CriteriaBuilder builder, Object arg) {
    if (arg == null) {
      throw new UnsupportedOperationException(NULL_ARG);
    } else if (arg instanceof LocalDate) {
      return builder.greaterThanOrEqualTo(root.get(property), LocalDate.parse(arg.toString()));
    } else if (arg instanceof LocalDateTime) {
      return builder.greaterThanOrEqualTo(root.get(property), LocalDateTime.parse(arg.toString()));
    } else {
      return builder.greaterThanOrEqualTo(root.get(property), arg.toString());
    }
  }

  private Predicate lessThanPredicate(Root<T> root, CriteriaBuilder builder, Object arg) {
    if (arg == null) {
      throw new UnsupportedOperationException(NULL_ARG);
    } else if (arg instanceof LocalDate) {
      return builder.lessThan(root.get(property), LocalDate.parse(arg.toString()));
    } else if (arg instanceof LocalDateTime) {
      return builder.lessThan(root.get(property), LocalDateTime.parse(arg.toString()));
    } else if (arg instanceof Instant) {
      return builder.lessThan(root.get(property), Instant.parse(arg.toString()));
    } else {
      return builder.lessThan(root.get(property), arg.toString());
    }
  }

  private Predicate lessThanOrEqualPredicate(Root<T> root, CriteriaBuilder builder, Object arg) {
    if (arg == null) {
      throw new UnsupportedOperationException(NULL_ARG);
    } else if (arg instanceof LocalDate) {
      return builder.lessThanOrEqualTo(root.get(property), LocalDate.parse(arg.toString()));
    } else if (arg instanceof LocalDateTime) {
      return builder.lessThanOrEqualTo(root.get(property), LocalDateTime.parse(arg.toString()));
    } else {
      return builder.lessThanOrEqualTo(root.get(property), arg.toString());
    }
  }

  private List<Object> castArguments(final Root<T> root) {
    Class<?> type = root.get(property).getJavaType();
    return arguments.stream().map(
        arg -> {
          if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Boolean.parseBoolean(arg);
          } else if (type.equals(Integer.class)) {
            return Integer.parseInt(arg);
          } else if (type.equals(Long.class)) {
            return Long.parseLong(arg);
          } else if (type.equals(LocalDate.class)) {
            return LocalDate.parse(arg);
          } else if (type.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(arg);
          } else if (type.equals(Instant.class)) {
            return Instant.parse(arg);
          } else {
            return arg;
          }
        }).collect(Collectors.toList());
  }
}
