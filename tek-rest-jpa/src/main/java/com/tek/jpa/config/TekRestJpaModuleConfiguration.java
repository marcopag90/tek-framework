package com.tek.jpa.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.tek.jpa.TekJpaAutoConfig;
import com.tek.shared.TekModuleConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
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
@Configuration
@ConditionalOnClass(value = {TekJpaAutoConfig.class})
public class TekRestJpaModuleConfiguration extends TekModuleConfiguration {

  public TekRestJpaModuleConfiguration(ApplicationContext context) {
    super(context);
  }

  @Override
  public void checkModuleConfiguration() {
    //noop
  }

  @Bean
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
