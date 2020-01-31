package com.tek.core.data

import com.tek.core.util.LabelEnum

enum class TekRunnerAction(override val label: String) : LabelEnum {

    CREATE("Execute data runner every time"),
    NONE("No action is taken")
}