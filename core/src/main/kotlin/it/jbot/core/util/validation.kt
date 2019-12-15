package it.jbot.core.util

import org.apache.commons.beanutils.PropertyUtilsBean

fun entityToMap(entity: Any): Map<String, Any?> = PropertyUtilsBean().describe(entity)