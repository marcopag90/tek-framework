package com.tek.core.controller.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Tek standard body error response, to wrap inside a {@link org.springframework.http.ResponseEntity}
 *
 * @author MarcoPagan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TekErrorResponse implements Serializable {

  private String message;
  private String path;
}
