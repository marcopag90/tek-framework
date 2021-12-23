package com.tek.jpa.controller.mock;

import com.tek.jpa.controller.impl.ReadOnlyDalController;
import com.tek.jpa.domain.Beer;
import com.tek.jpa.service.ReadOnlyDalService;
import com.tek.jpa.service.mock.BeerReadOnlyDalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BeerReadOnlyDalController.PATH)
public class BeerReadOnlyDalController extends ReadOnlyDalController<Beer, Long> {

  public static final String PATH = "/BeerReadOnlyCrudController";

  @Override
  public ReadOnlyDalService<Beer, Long> dalService() {
    return context.getBean(BeerReadOnlyDalService.class);
  }

  @Override
  public boolean readAuthorized() {
    return true;
  }
}
