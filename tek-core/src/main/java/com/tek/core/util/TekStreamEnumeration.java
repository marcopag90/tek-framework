package com.tek.core.util;

import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Allows to open a stream over {@link Enumeration} (patch for Java 1.8). This feature is
 * jdk-avaiable from 1.9
 *
 * @author MarcoPagan
 */
public class TekStreamEnumeration {

  private TekStreamEnumeration() {
  }

  public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
    return StreamSupport.stream(
        new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
          public boolean tryAdvance(Consumer<? super T> action) {
            if (e.hasMoreElements()) {
              action.accept(e.nextElement());
              return true;
            }
            return false;
          }

          @Override
          public void forEachRemaining(Consumer<? super T> action) {
            while (e.hasMoreElements()) {
              action.accept(e.nextElement());
            }
          }
        }, false);
  }
}
