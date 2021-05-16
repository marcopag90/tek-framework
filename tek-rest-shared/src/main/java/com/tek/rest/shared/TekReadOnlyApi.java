package com.tek.rest.shared;

public interface TekReadOnlyApi extends TekAuthorizedApi {

  String CAN_READ = "this.readAuthorized()";

  default boolean readAuthorized() {
    return isAuthorized();
  }

}
