package it.jbot.shared.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Json Deserializer to _trim_ all incoming JSON parameters from REST services
 */
@Component
public class JsonDeserializerConf extends SimpleModule {

    public JsonDeserializerConf() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {

                return jsonParser.getValueAsString().trim();
            }
        });
    }


}
