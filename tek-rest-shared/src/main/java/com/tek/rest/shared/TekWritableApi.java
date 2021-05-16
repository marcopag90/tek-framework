package com.tek.rest.shared;

/**
 * Base interface to provide <i>crud AOP</i> methods.
 *
 * @author MarcoPagan
 */
public interface TekWritableApi extends TekReadOnlyApi {

  String CAN_CREATE = "this.createAuthorized()";
  String CAN_UPDATE = "this.updateAuthorized()";
  String CAN_DELETE = "this.deleteAuthorized()";

  default boolean createAuthorized() {
    return isAuthorized();
  }

  default boolean updateAuthorized() {
    return isAuthorized();
  }

  default boolean deleteAuthorized() {
    return isAuthorized();
  }

}
