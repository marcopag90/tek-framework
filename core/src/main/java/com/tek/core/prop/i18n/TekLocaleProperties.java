package com.tek.core.prop.i18n;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties to manage app locale resolution.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Getter
@Setter
public class TekLocaleProperties {

    private TekLocaleType type = TekLocaleType.SESSION;
    private String cookieName = "locale";
    private Integer cookieMaxAge = -1;

    public enum TekLocaleType {
        SESSION, COOKIE, ACCEPTH_HEADER
    }
}

