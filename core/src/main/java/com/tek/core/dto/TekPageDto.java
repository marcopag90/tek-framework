package com.tek.core.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

/**
 * Tek paged body response, providing:
 * <p>
 * - content: the page content
 * <p>
 * - totalElements: query count() result
 * <p>
 * - totalPages: number of total pages
 *
 * @author MarcoPagan
 */
@NoArgsConstructor
@Getter
@Setter
public class TekPageDto<T> implements Serializable {

  private transient List<T> content;
  private long totalElements;
  private int totalPages;

  public TekPageDto(Page<T> page) {
    this.content = page.getContent();
    this.totalElements = page.getTotalElements();
    this.totalPages = page.getTotalPages();
  }
}
