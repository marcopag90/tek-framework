package com.tek.core.properties.web;

import lombok.Data;

/**
 * Configuration properties for web content.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekWebProperties {

  /**
   * Disable to avoid web application default entrypoint
   */
  private boolean enabled = true;

  /**
   * Root path of the web application
   */
  private String rootPath = "/";

  /**
   * Main page of the web application
   */
  private String indexPage = "index.html";

}
