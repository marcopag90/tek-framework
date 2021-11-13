package com.tek.rest.shared.api;

/**
 * Base interface to provide <i>crud AOP</i> methods.
 *
 * @author MarcoPagan
 */
public interface TekWritableApi extends TekReadOnlyApi {

  String CAN_CREATE = "this.createAuthorized()";
  String CAN_UPDATE = "this.updateAuthorized()";
  String CAN_DELETE = "this.deleteAuthorized()";

  boolean createAuthorized();

  boolean updateAuthorized();

  boolean deleteAuthorized();

}
