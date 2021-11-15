package com.tek.rest.shared.api;

/**
 * Base interface to provide read-only <i>AOP</i> methods.
 *
 * @author MarcoPagan
 */
public interface ReadOnlyApi {

  String CAN_READ = "this.readAuthorized()";

  boolean readAuthorized();
}
