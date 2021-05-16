package com.tek.jpa.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.tek.jpa.TekEntityManager;
import com.tek.jpa.TekJpaAutoConfig;
import com.tek.shared.TekModuleConfiguration;
import javax.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration providing:
 * <ul>
 *   <li>{@link Hibernate5Module} to avoid MappingJackson2HttpMessageConverter serialization
 *    failure on lazy proxy objects retrieved from Hibernate, when no session is in context.
 *  </li>
 *  <li>
 *    {@link TekEntityManager} as a bean extending {@link EntityManager} to expose some utility
 *    methods.
 *  </li>
 * </ul>
 *
 * @author MarcoPagan
 */
@Configuration
@ConditionalOnClass(value = {TekJpaAutoConfig.class})
public class TekJpaModuleConfiguration extends TekModuleConfiguration {

  public TekJpaModuleConfiguration(ApplicationContext context) {
    super(context);
  }

  @Override
  public void checkModuleConfiguration() {
    //noop
  }

  @Bean
  public TekEntityManager tekEntityManager(EntityManager manager, ApplicationContext context) {
    return new TekEntityManager(manager, context);
  }

  @Bean
  @Conditional(HibernateConditional.class)
  public Module hibernate5Module() {
    return new Hibernate5Module();
  }
}
