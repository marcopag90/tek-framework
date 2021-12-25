package com.tek.jpa.controller.mock;

import com.tek.jpa.controller.impl.WritableDalController;
import com.tek.jpa.domain.Author;
import com.tek.jpa.service.WritableDalService;
import com.tek.jpa.service.mock.AuthorWritableDalService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthorWritableDalController.PATH)
public class AuthorWritableDalController extends WritableDalController<Author, Integer> {

  public static final String PATH = "/AuthorWritableDalController";

  @Override
  public boolean createAuthorized() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("AUTHOR_CREATE"));
  }

  @Override
  public boolean readAuthorized() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("AUTHOR_READ"));
  }

  @Override
  public boolean updateAuthorized() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("AUTHOR_UPDATE"));
  }

  @Override
  public boolean deleteAuthorized() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("AUTHOR_DELETE"));
  }

  @Override
  protected WritableDalService<Author, Integer> service() {
    return context.getBean(AuthorWritableDalService.class);
  }
}
