package com.tek.jpa.controller;

import com.tek.jpa.controller.impl.ReadOnlyCrudController;
import com.tek.jpa.domain.Author;
import com.tek.jpa.service.AuthorReadOnlyDalService;
import com.tek.jpa.service.impl.BaseReadOnlyDalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthorReadyOnlyCrudController.PATH)
public class AuthorReadyOnlyCrudController extends ReadOnlyCrudController<Author, Integer> {

  public static final String PATH = "/AuthorUserReadyOnlyCrudController";

  @Autowired
  private AuthorReadOnlyDalService dalService;

  @Override
  public boolean readAuthorized() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("AUTHOR_READ"));
  }

  @Override
  protected BaseReadOnlyDalService<Author, Integer> getDalService() {
    return dalService;
  }
}
