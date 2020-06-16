package com.tek.audit;

/**
 * Tek Audit Constants
 *
 * @author MarcoPagan
 */
public interface TekAuditConstant {

    /**
     * Tek Audit Module Configuration name
     */
    String TEK_AUDIT_CONFIGURATION = "TekAuditConfiguration";

    /**
     * Classpath location for i18n message bundle
     */
    String TEK_AUDIT_MESSAGE_BUNDLE = "classpath:/i18n/audit_messages";

    /**
     * Default name of i18n message bundle
     */
    String TEK_AUDIT_MESSAGE_SOURCE = "com.tek.audit.messageSource";

    /**
     * Prefix of the .yaml/.properties for Tek Core Module configuration.
     */
    String TEK_AUDIT = "tek.audit";

    /**
     * Locale change API path
     */
    String TEK_WEB_AUDIT_PATH = "/web-audit";
}
