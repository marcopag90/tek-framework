package com.tek.rest.shared.swagger;

/**
 * Utility interface that <i>MUST</i> be implemented by Spring {@link
 * org.springframework.stereotype.Component} to expose Swagger ignored class types.
 * <p>
 *
 * @author MarcoPagan
 */
public interface SwaggerIgnore {

  Class<?>[] ignore();
}
