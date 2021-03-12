package com.tek.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Default <b>CRUD</b> paths declared over a {@link RequestMapping}
 */
public class TekMapping {

  private TekMapping() {
  }

  public static final String CREATE = "/create";
  public static final String READ = "/read";
  public static final String LIST = "/list";
  public static final String UPDATE = "/update";
  public static final String DELETE = "/delete";
}
