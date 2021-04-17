package com.tek.core.dto;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tek standard body error dto, to wrap inside a {@link org.springframework.http.ResponseEntity}
 *
 * @author MarcoPagan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TekErrorDto implements Serializable {

  private String message;
  private String path;
}
