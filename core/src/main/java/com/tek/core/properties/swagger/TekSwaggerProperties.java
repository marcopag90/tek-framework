package com.tek.core.properties.swagger;

import lombok.Data;

/**
 * Configuration properties for Swagger.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekSwaggerProperties {

  public static final String DEFAULT_DESCRIPTION = "Tek Framework API Service";

  private String description = DEFAULT_DESCRIPTION;
  private String termOfServiceUrl = null;

  private String contactName = null;
  private String contactUrl = null;
  private String contactEmail = null;

  private String license = null;
  private String licenseUrl = null;
}
