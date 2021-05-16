package com.tek.rest.shared;

/**
 * Base interface to provide read-only <i>AOP</i> methods.
 *
 * @author MarcoPagan
 */
public interface TekReadOnlyApi extends TekAuthorizedApi {

  String CAN_READ = "this.readAuthorized()";

  default boolean readAuthorized() {
    return isAuthorized();
  }

}
