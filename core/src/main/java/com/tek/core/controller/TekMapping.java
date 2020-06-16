package com.tek.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Default <b>CRUD</b> paths declared over a {@link RequestMapping}
 */
public interface TekMapping {

    String CREATE = "/create";
    String READ = "/read";
    String LIST = "/list";
    String UPDATE = "/update";
    String DELETE = "/delete";
}
