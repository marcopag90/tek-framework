package it.jbot.core.util

import java.io.InputStream

/**
 * Shorthand for reading an InputStream as a String
 */
fun InputStream.readText(): String = reader().readText()