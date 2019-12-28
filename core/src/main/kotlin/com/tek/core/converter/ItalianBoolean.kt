package com.tek.core.converter

import javax.persistence.AttributeConverter


/**
 * Italian Boolean Type Conversion.
 *
 * This implementation considers anything different from character "S" including **null**
 * as false
 */
class ItalianBoolean : AttributeConverter<Boolean, String> {
    
    override fun convertToDatabaseColumn(attribute: Boolean?): String {
        return if (attribute != null && attribute) "S" else "N"
    }
    
    override fun convertToEntityAttribute(dbData: String): Boolean? {
        return "S" == dbData
    }
}