package com.tek.core.config.swagger;

/**
 * Utility interface that <i>MUST</i> be implemented
 * by Spring {@link org.springframework.stereotype.Component} to expose Swagger ignored class types.
 * <p>
 * See {@link TekCoreSwaggerIgnore} for an example.
 *
 * @author MarcoPagan
 */
public interface SwaggerIgnore {

    Class<?>[] ignore();
}
