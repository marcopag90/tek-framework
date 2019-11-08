package it.jbot.shared.util

import javax.persistence.AttributeConverter


/**
 * Italian Boolean Type Conversion.
 *
 * This implementation considers anything different from character "S" including **null**
 * as false
 */
class ItalianBooleanStringConverter : AttributeConverter<Boolean, String> {
    
    override fun convertToDatabaseColumn(attribute: Boolean?): String {
        return if (attribute != null && attribute) "S" else "N"
    }
    
    override fun convertToEntityAttribute(dbData: String): Boolean? {
        return "S" == dbData
    }
}