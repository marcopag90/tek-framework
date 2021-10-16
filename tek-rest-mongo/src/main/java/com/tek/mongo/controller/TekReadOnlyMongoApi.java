package com.tek.mongo.controller;

import static com.tek.rest.shared.constants.TekRestSharedConstants.FILTER_NAME;

import com.tek.rest.shared.api.TekReadOnlyApi;
import com.turkraft.springfilter.boot.Filter;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface TekReadOnlyMongoApi<T, I> extends TekReadOnlyApi {

  @GetMapping
  @PreAuthorize(CAN_READ)
  Page<T> findAll(@Filter(parameterName = FILTER_NAME) Document where, Pageable pageable);

  @GetMapping("/{id}")
  @PreAuthorize(CAN_READ)
  T findById(@PathVariable I id);

}
