package com.tek.core.properties;

import lombok.Data;

/**
 * Configuration properties for email handling.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekMailProperties {

  private boolean sendErrors;
  private boolean realDelivery;
}
