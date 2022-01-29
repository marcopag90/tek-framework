package com.tek.core.properties.i18n;

import lombok.Data;

/**
 * Configuration properties to manage locale resolution.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekLocaleProperties {

  @TekLocaleTypeConstraint
  private String type = TekLocaleConstants.SESSION;
  private String postParameterName = "locale";
  private String cookieName = "locale";
  private Integer cookieMaxAge = -1;
}

