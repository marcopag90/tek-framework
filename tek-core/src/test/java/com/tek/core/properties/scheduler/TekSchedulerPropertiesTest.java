package com.tek.core.properties.scheduler;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.core.properties.TekCoreProperties;
import lombok.val;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@TestPropertySource("classpath:scheduler-custom.properties")
@TestMethodOrder(OrderAnnotation.class)
class TekSchedulerPropertiesTest {

  @Autowired
  private TekCoreProperties coreCustomProperties;

  @Test
  @Order(1)
  void defaultValues() {
    val properties = new TekCoreProperties().getScheduler();
    assertNotNull(properties);
    assertTrue(properties.getActive());
  }

  @Test
  @Order(2)
  void customValues() {
    val properties = coreCustomProperties.getScheduler();
    assertNotNull(properties);
    assertTrue(properties.getActive());
  }

}
