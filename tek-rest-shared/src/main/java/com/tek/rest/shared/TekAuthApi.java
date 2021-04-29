package com.tek.rest.shared;

import org.springframework.security.core.context.SecurityContextHolder;

public interface TekAuthApi {

  String CAN_CREATE = "this.createAuthorized()";
  String CAN_READ = "this.readAuthorized()";
  String CAN_UPDATE = "this.updateAuthorized()";
  String CAN_DELETE = "this.deleteAuthorized()";

  boolean createAuthorized();

  boolean readAuthorized();

  boolean updateAuthorized();

  boolean deleteAuthorized();

  default boolean isAuthorized() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

}
