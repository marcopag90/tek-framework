package com.tek.jpa.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition to detect hibernate implementation of JPA.
 *
 * @author MarcoPagan
 */
public class HibernateConditional implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    try {
      Class.forName("org.hibernate.internal.SessionImpl");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
