package com.tek.core.prop;

import com.tek.core.util.TekLabelEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.stream.Stream;

/**
 * Configuration properties for CORS Filter.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Getter
@Setter
public class TekCorsProperties {

    private String allowedOrigin = "http://localhost:4200";
    private Boolean allowedCredentials = true;
    private String[] allowedMethods =
        Stream.of(RequestMethod.values()).map(RequestMethod::name).toArray(String[]::new);
    private String[] allowedHeaders =
        Stream.of(RequestHeader.values()).map(RequestHeader::label).toArray(String[]::new);

    enum RequestHeader implements TekLabelEnum {

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

        @Override
        public String label() {
            return this.label;
        }
    }
}
