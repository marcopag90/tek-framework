package com.tek.core.properties.i18n;

/**
 * Configuration properties to manage locale resolution.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
public class TekLocaleProperties {

  @TekLocaleTypeConstraint
  private String type = TekLocaleConstants.SESSION;
  private String postParameterName = "locale";
  private String cookieName = "locale";
  private Integer cookieMaxAge = -1;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPostParameterName() {
    return postParameterName;
  }

  public void setPostParameterName(String postParameterName) {
    this.postParameterName = postParameterName;
  }

  public String getCookieName() {
    return cookieName;
  }

  public void setCookieName(String cookieName) {
    this.cookieName = cookieName;
  }

  public Integer getCookieMaxAge() {
    return cookieMaxAge;
  }

  public void setCookieMaxAge(Integer cookieMaxAge) {
    this.cookieMaxAge = cookieMaxAge;
  }

}

