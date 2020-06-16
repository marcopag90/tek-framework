package com.tek.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.tek.audit.TekAuditConstant.TEK_AUDIT;

/**
 * Tek Audit Module properties to be evaluated from application.yaml / application.properties files.
 *
 * @author MarcoPagan
 */
@Configuration
@ConfigurationProperties(prefix = TEK_AUDIT)
@Getter
@Setter
public class TekAuditProperties {

    /**
     * Parameter to determine the max payload length to persist or to log.
     **/
    private int payloadLength = 4096;

}
