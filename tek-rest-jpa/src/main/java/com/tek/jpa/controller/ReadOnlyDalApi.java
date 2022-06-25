package com.tek.jpa.controller;

import static com.tek.rest.shared.constants.TekRestSharedConstants.FILTER_NAME;

import com.tek.rest.shared.dto.ApiPage;
import com.tek.rest.shared.exception.EntityNotFoundException;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Interface to provide a read-only <i>CRUD</i> JPA Rest API.
 *
 * @param <E> : a concrete {@link javax.persistence.Entity}
 * @author MarcoPagan
 */
public interface ReadOnlyDalApi<E extends Serializable, I extends Serializable> {

  boolean readAuthorized();

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("this.readAuthorized()")
  ApiPage<E> findAll(
      @Filter(parameterName = FILTER_NAME)
      @Parameter(
          name = FILTER_NAME,
          in = ParameterIn.QUERY,
          description = "search query",
          schema = @Schema(implementation = String.class),
          allowEmptyValue = true
      ) Specification<E> spec,
      @ParameterObject Pageable page
  );
  /*
  allowEmptyValue to avoid 'required' parameter from the swagger-ui, see here:
  https://github.com/springdoc/springdoc-openapi/issues/252.
  @RequestParam could be declared to avoid this in a clean way as stated in the issue, but it
  turns off the SpecificationFilterArgumentResolver and places an MVC converter that fails to convert
  the Specification (because there's no implementation provided by default).
  Writing our own implementation is redundant, since the filter binding logic is already defined
  in the SpecificationFilterArgumentResolver.
  */

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("this.readAuthorized()")
  E findById(@PathVariable("id") I id) throws EntityNotFoundException;
}
