package com.tek.core.config.swagger;

import org.springframework.stereotype.Component;

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
    public Class<?>[] ignore() {
        return new Class[]{};
    }
}
