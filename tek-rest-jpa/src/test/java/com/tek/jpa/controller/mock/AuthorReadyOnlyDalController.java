package com.tek.jpa.controller.mock;

import com.tek.jpa.controller.ReadOnlyDalController;
import com.tek.jpa.domain.Author;
import com.tek.jpa.service.ReadOnlyDalService;
import com.tek.jpa.service.mock.AuthorReadOnlyDalService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthorReadyOnlyDalController.PATH)
public class AuthorReadyOnlyDalController extends ReadOnlyDalController<Author, Integer> {

  public static final String PATH = "/AuthorUserReadyOnlyCrudController";

  @Override
  public boolean readAuthorized() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("AUTHOR_READ"));
  }

  @Override
  public ReadOnlyDalService<Author, Integer> service() {
    return context.getBean(AuthorReadOnlyDalService.class);
  }
}
