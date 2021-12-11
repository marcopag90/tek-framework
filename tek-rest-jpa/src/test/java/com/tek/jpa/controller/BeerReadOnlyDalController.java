package com.tek.jpa.controller;

import com.tek.jpa.controller.impl.BaseReadOnlyDalController;
import com.tek.jpa.domain.Beer;
import com.tek.jpa.service.BeerReadOnlyDalService;
import com.tek.jpa.service.impl.BaseReadOnlyDalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BeerReadOnlyDalController.PATH)
public class BeerReadOnlyDalController extends BaseReadOnlyDalController<Beer, Long> {

  public static final String PATH = "/BeerReadOnlyCrudController";

  @Override
  public BaseReadOnlyDalService<Beer, Long> getService() {
    return context.getBean(BeerReadOnlyDalService.class);
  }

  @Override
  public boolean readAuthorized() {
    return true;
  }
}
