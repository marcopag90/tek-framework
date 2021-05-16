package com.tek.rest.shared;

import org.springframework.security.core.context.SecurityContextHolder;

public interface TekAuthorizedApi {

  default boolean isAuthorized() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

}
