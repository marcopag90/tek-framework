package com.tek.jpa.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.tek.jpa.TekEntityManager;
import com.tek.shared.TekModuleConfiguration;
import javax.persistence.EntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to provide
 * <ul>
 *   <li>{@link Hibernate5Module} to avoid MappingJackson2HttpMessageConverter serialization
 *    failure on lazy proxy objects retrieved from Hibernate, when no session is in context.
 *  </li>
 *  <li>
 *    {@link TekEntityManager} as bean extending {@link EntityManager} to provide some utility
 *    methods.
 *  </li>
 * </ul>
 *
 * @author MarcoPagan
 */
@Configuration
public class TekJpaAutoConfig extends TekModuleConfiguration {

  public TekJpaAutoConfig(ApplicationContext context) {
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
