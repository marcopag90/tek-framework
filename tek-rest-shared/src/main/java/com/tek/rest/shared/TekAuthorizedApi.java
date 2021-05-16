package com.tek.rest.shared;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>Base interface to provide a default authorization behaviour for a <i>Restful API</i>.
 * <p>Developers can override this method (eg. inside a Controller implementation)
 * to globally customize all methods authorizations.
 *
 * @author MarcoPagan
 */
public interface TekAuthorizedApi {

  default boolean isAuthorized() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

}
