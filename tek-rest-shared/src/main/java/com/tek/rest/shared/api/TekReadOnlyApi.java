package com.tek.rest.shared.api;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Base interface to provide read-only <i>AOP</i> methods.
 *
 * @author MarcoPagan
 */
public interface TekReadOnlyApi {

  String CAN_READ = "this.readAuthorized()";

  default boolean readAuthorized() {
    return isAuthorized();
  }

  /**
   * Function to globally customize api authorizations.
   */
  default boolean isAuthorized() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }
}
