package com.tek.core.dto;


import java.io.Serializable;

/**
 * Tek standard body error dto, to wrap inside a {@link org.springframework.http.ResponseEntity}
 *
 * @author MarcoPagan
 */
public record TekErrorDto(
    String message,
    String path
) implements Serializable {

}
