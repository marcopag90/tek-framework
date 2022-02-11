package com.tek.rest.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

/**
 * Default DTO returned from paged Rest API calls.
 *
 * @author MarcoPagan
 */
public class ApiPage<T extends Serializable> implements Serializable {

  @JsonIgnore
  private final transient Page<T> page;
  private final List<T> content;
  @JsonProperty("currentPage")
  private final int currentPage;
  @JsonProperty("pageSize")
  private final int pageSize;
  @JsonProperty("totalPages")
  private final int totalPages;
  @JsonProperty("totalElements")
  private final long totalElements;
  @JsonProperty("isLastPage")
  private final boolean isLastPage;

  public ApiPage(@NonNull Page<T> page) {
    Objects.requireNonNull(page);
    this.page = page;
    content = page.getContent();
    currentPage = page.getNumber();
    pageSize = page.getSize();
    totalPages = page.getTotalPages();
    totalElements = page.getTotalElements();
    isLastPage = page.isLast();
  }

  public Page<T> getPage() {
    return page;
  }

  public List<T> getContent() {
    return content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getPageSize() {
    return pageSize;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public boolean isLastPage() {
    return isLastPage;
  }
}
