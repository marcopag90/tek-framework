package it.jbot.shared

infix fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}