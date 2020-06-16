package com.tek.core.conf.swagger;

import com.tek.core.controller.api.TekResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tek Core Swagger ignore component.
 * <p>
 * This component is collected by {@link TekSwaggerIgnoreConfiguration}.
 *
 * @author MarcoPagan
 */

@Component
public class TekCoreSwaggerIgnore implements SwaggerIgnore {

    @Override
    public List<Class<?>> ignore() {
        return new ArrayList<>(Collections.singleton(TekResponse.class));
    }
}
