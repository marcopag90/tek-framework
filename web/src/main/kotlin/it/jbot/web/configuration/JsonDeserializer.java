package it.jbot.web.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Json Deserializer to _trim_ all incoming JSON parameters from REST services
 */
@Configuration
public class JsonDeserializer extends SimpleModule {

    public JsonDeserializer() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {

                return jsonParser.getValueAsString().trim();
            }
        });
    }
}
