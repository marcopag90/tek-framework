package com.tek.shared.function;

import java.util.function.Function;

/**
 * Functional interface to allow wrapping of exceptions inside lambdas.
 *
 * @author MarcoPagan
 */
@SuppressWarnings({"squid:S1181", "squid:S112"})
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

  R apply(T t) throws E;

  static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> f) {
    return t -> {
      try {
        return f.apply(t);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    };
  }
}