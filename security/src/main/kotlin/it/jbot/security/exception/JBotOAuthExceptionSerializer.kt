package it.jbot.security.exception

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import it.jbot.core.util.JBotDateUtils.jbotTimestamp
import it.jbot.security.oauth.exception.JBotOAuthException

/**
 * JBot Serializer for {@link JBotOAuthException}
 * <ol>
 *     <li>timestamp: the timestamp of the event</li>
 *     <li>status: the Integer value for {@link org.springframework.http.HttpStatus}</li>
 *     <li>errors: array of error messages provided by the base {@link OAuth2Exception}</li>
 * </ol>
 */
class JBotOAuthExceptionSerializer : StdSerializer<JBotOAuthException>(JBotOAuthException::class.java) {

    override fun serialize(
        value: JBotOAuthException,
        generator: JsonGenerator,
        provider: SerializerProvider?
    ) {
        generator.apply {

            writeStartObject()
            writeObjectField("timestamp", jbotTimestamp())
            writeNumberField("status", value.httpErrorCode)
            writeObjectField("errors", listOf(value.message))
            value.additionalInformation?.let {
                for (obj in it)
                    generator.writeStringField(obj.key, obj.value)
            }
            writeEndObject()
        }
    }
}