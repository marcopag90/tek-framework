package com.tek.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tek.jpa")
public class TekRestJpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(TekRestJpaApplication.class, args);
  }

}
