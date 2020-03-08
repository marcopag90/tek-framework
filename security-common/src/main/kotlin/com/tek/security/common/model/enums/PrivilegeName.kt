package com.tek.security.common.model.enums

import com.tek.core.util.LabelEnum

enum class PrivilegeName(
    override val label: String
) : LabelEnum {

    /*
    Client privileges
     */
    DASHBOARD("privilege_dashboard"),
    MENU("privilege_menu"),

    /*
    Crud privileges
     */
    ROLE_READ("privilege_role_read"),

    USER_CREATE("privilege_user_create"),
    USER_READ("privilege_user_read"),
    USER_UPDATE("privilege_user_update"),
    USER_DELETE("privilege_user_delete"),

    AUDIT_READ("privilege_audit_read"),

    NOTIFICATION_READ("privilege_notification_read"),
    NOTIFICATION_UPDATE("privilege_notification_update"),
    NOTIFICATION_DELETE("privilege_notification_delete")
}