package it.jbot.security.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import it.jbot.shared.util.JBotDateUtils;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * JBot Serializer for {@link JBotOAuthException}
 * <ol>
 *     <li>timestamp: the timestamp of the event</li>
 *     <li>status: the Integer value for {@link org.springframework.http.HttpStatus}</li>
 *     <li>errors: array of error messages provided by the base {@link OAuth2Exception}</li>
 * </ol>
 */
public class JBotOAuthExceptionSerializer extends StdSerializer<JBotOAuthException> {

    public JBotOAuthExceptionSerializer() {

        super(JBotOAuthException.class);
    }

    @Override
    public void serialize(
            JBotOAuthException value,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {

        generator.writeStartObject();

        generator.writeObjectField("timestamp", JBotDateUtils.jbotTimestamp());
        generator.writeObjectField("errors", Arrays.asList(value.getMessage()));

        if (value.getAdditionalInformation() != null)
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet())
                generator.writeStringField(entry.getKey(), entry.getValue());

        generator.writeEndObject();
    }
}

