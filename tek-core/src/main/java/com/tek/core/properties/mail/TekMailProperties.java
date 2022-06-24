package com.tek.core.properties.mail;

/**
 * Configuration properties for email handling.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
public class TekMailProperties {

  private boolean realDelivery;

  public boolean isRealDelivery() {
    return realDelivery;
  }

  public void setRealDelivery(boolean realDelivery) {
    this.realDelivery = realDelivery;
  }
}
