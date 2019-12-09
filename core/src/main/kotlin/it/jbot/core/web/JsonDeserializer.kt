package it.jbot.core.web

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer

import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Configuration
import java.io.IOException

/**
 * Json Deserializer to _trim_ all incoming JSON parameters from REST services
 */
@Configuration
class JsonDeserializer : SimpleModule() {
    init {
        this.addDeserializer(
            String::class.java,
            object : StdScalarDeserializer<String?>(String::class.java) {
                @Throws(IOException::class)
                override fun deserialize(jsonParser: JsonParser, ctx: DeserializationContext)
                        : String = jsonParser.valueAsString.trim()
            })
    }
}