package it.jbot.core

import it.jbot.core.util.LabelEnum

/**Spring profiles for business logics*/
enum class SpringProfile(override val label: String) : LabelEnum {
    
    DEVELOPMENT("dev"),
    TEST("test"),
    PRODUCTION("prod")
}