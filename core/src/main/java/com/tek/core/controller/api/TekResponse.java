package com.tek.core.controller.api;

import lombok.*;

import java.io.Serializable;

/**
 * Tek standard body response, to wrap inside a {@link org.springframework.http.ResponseEntity}
 *
 * @author MarcoPagan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TekResponse implements Serializable {

    private String message;
    private Object body;
}
