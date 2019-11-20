package it.jbot.shared

import it.jbot.shared.util.LabelEnum

/**Spring profiles for business logics*/
enum class SpringProfile(override val label: String) : LabelEnum {
    
    DEVELOPMENT("dev"),
    TEST("test"),
    PRODUCTION("prod")
}