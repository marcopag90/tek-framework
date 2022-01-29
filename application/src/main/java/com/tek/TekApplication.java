package com.tek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.tek.core.TekCoreConstant.TEK_PACKAGES_TO_SCAN;

/**
 * Class to bootstrap the spring application
 * <ul>
 *     <li>{@link EnableAsync}: required to execute code asynchronously</li>
 *     <li>{@link EnableScheduling}: required to execute scheduled tasks</li>
 *     <li>{@link EnableJpaAuditing}: required to perform auditing of JPA
 *         {@link javax.persistence.Entity}
 *     </li>
 * </ul>
 */
@SpringBootApplication(scanBasePackages = TEK_PACKAGES_TO_SCAN)
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
public class TekApplication {

  public static void main(String[] args) {
    SpringApplication.run(TekApplication.class);
  }
}


