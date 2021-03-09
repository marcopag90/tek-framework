package com.tek.core.properties.i18n;

import lombok.Data;

/**
 * Configuration properties to manage app locale resolution.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekLocaleProperties {

    private TekLocaleType type = TekLocaleType.SESSION;
    private String cookieName = "locale";
    private Integer cookieMaxAge = -1;

    public enum TekLocaleType {
        SESSION, COOKIE, ACCEPTH_HEADER
    }
}

