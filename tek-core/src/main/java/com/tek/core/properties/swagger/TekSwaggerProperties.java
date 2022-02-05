package com.tek.core.properties.swagger;

/**
 * Configuration properties for Swagger.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
public class TekSwaggerProperties {

  public static final String DEFAULT_DESCRIPTION = "Tek Framework API Service";

  private String description = DEFAULT_DESCRIPTION;
  private String termOfServiceUrl = null;

  private String contactName = null;
  private String contactUrl = null;
  private String contactEmail = null;

  private String license = null;
  private String licenseUrl = null;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTermOfServiceUrl() {
    return termOfServiceUrl;
  }

  public void setTermOfServiceUrl(String termOfServiceUrl) {
    this.termOfServiceUrl = termOfServiceUrl;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactUrl() {
    return contactUrl;
  }

  public void setContactUrl(String contactUrl) {
    this.contactUrl = contactUrl;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getLicenseUrl() {
    return licenseUrl;
  }

  public void setLicenseUrl(String licenseUrl) {
    this.licenseUrl = licenseUrl;
  }

}
