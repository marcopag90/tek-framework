package com.tek.core.properties;

import java.util.stream.Stream;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Configuration properties for CORS Filter.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekCorsProperties {

  private String allowedOrigin = "http://localhost:4200";
  private Boolean allowedCredentials = true;
  private String[] allowedMethods =
      Stream.of(RequestMethod.values()).map(RequestMethod::name).toArray(String[]::new);
  private String[] allowedHeaders =
      Stream.of(RequestHeader.values()).map(RequestHeader::label).toArray(String[]::new);

  enum RequestHeader {

    X_REQUESTED_WITH("x-requested-with"),
    L_AUTHORIZATION("authorization"),
    CONTENT_TYPE("Content-Type"),
    U_AUTHORIZATION("Authorization"),
    CREDENTIAL("credential"),
    X_XSRF_TOKEN("X-XSRF-TOKEN");

    private final String label;

    RequestHeader(String label) {
      this.label = label;
    }

    public String label() {
      return this.label;
    }
  }
}
