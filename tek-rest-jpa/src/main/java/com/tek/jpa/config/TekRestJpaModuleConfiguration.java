package com.tek.jpa.config;

import static com.tek.jpa.constants.TekRestJpaBeanNames.TEK_REST_JPA_CONFIGURATION;
import static com.tek.jpa.constants.TekRestJpaBeanNames.TEK_REST_JPA_HIBERNATE_5_MODULE;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * <ul>
 *   <li>Provides a {@link Hibernate5Module} to avoid MappingJackson2HttpMessageConverter serialization
 *    failure on lazy proxy objects retrieved from Hibernate, when no session is in context.
 *  </li>
 * </ul>
 *
 * @author MarcoPagan
 */
@Configuration(TEK_REST_JPA_CONFIGURATION)
public class TekRestJpaModuleConfiguration {

  @Bean(TEK_REST_JPA_HIBERNATE_5_MODULE)
  @Conditional(HibernateConditional.class)
  public Module hibernate5Module() {
    return new Hibernate5Module();
  }

  /**
   * Condition to detect hibernate implementation of JPA.
   */
  static class HibernateConditional implements Condition {

    @Override
    public boolean matches(
        @NonNull ConditionContext context,
        @NonNull AnnotatedTypeMetadata metadata
    ) {
      try {
        Class.forName("org.hibernate.internal.SessionImpl");
        return true;
      } catch (ClassNotFoundException e) {
        return false;
      }
    }
  }
}
