package it.jbot.security.model.enums

import it.jbot.core.util.LabelEnum

enum class PrivilegeName(
    override val label: String
) : LabelEnum {

    DASHBOARD("privilege_dashboard"),
    MENU("privilege_menu"),

    ROLE_READ("privilege_role_read"),

    USER_CREATE("privilege_user_create"),
    USER_READ("privilege_user_read"),
    USER_UPDATE("privilege_user_update"),
    USER_DELETE("privilege_user_delete")
}