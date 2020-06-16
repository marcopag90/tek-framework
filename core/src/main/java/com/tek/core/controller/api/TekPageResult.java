package com.tek.core.controller.api;

import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Tek paged body response, to wrap inside a {@link TekResponse} body
 *
 * @author MarcoPagan
 */
@NoArgsConstructor
@Getter
@Setter
public class TekPageResult<T> implements Serializable {

    private List<T> content;
    private Long totalElements;
    private Integer totalPages;

    public TekPageResult(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
