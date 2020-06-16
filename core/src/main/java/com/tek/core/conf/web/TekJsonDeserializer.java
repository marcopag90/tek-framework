package com.tek.core.conf.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Json Deserializer to trim all incoming JSON parameters from REST services
 *
 * @author MarcoPagan
 */
@Configuration
public class TekJsonDeserializer extends SimpleModule {

    public TekJsonDeserializer() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
                return jsonParser.getValueAsString().trim();
            }
        });
    }
}
