package com.tek.core.properties.web;

/**
 * Configuration properties for web content.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
public class TekWebProperties {

  /**
   * Disable to avoid web application default entrypoint
   */
  private boolean enabled = true;

  /**
   * Main page of the web application
   */
  private String indexPage = "index.html";

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getIndexPage() {
    return indexPage;
  }

  public void setIndexPage(String indexPage) {
    this.indexPage = indexPage;
  }
}
