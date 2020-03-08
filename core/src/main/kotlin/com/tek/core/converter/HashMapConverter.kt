package com.tek.core.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import java.lang.Exception
import javax.persistence.AttributeConverter

/**
 * Hashmap String Type Conversion.
 *
 *
 * This implementation converts a string representation of a map in a database (like plain JSON objects)
 * to a more friendly user HashMap java type.
 */
class HashMapConverter : AttributeConverter<HashMap<String, Any>, String> {

    companion object {
        private val log = LoggerFactory.getLogger(HashMapConverter::class.java)
    }

    override fun convertToDatabaseColumn(attribute: HashMap<String, Any>): String {
        var jsonData = ""
        try {
            jsonData = jacksonObjectMapper().writeValueAsString(attribute)
        } catch (ex: Exception) {
            log.error("JSON writing error: $attribute")
        }
        return jsonData
    }

    override fun convertToEntityAttribute(dbData: String?): HashMap<String, Any> {
        var mapData = hashMapOf<String, Any>()
        try {
            mapData = jacksonObjectMapper().readValue(dbData, mapData::class.java)
        } catch (ex: Exception) {
            log.error("JSON reading error: $dbData")
        }
        return mapData
    }

}