package com.tek.rest.shared.controller;

import com.tek.rest.shared.constants.TekRestSharedConstants;
import io.swagger.v3.oas.annotations.Operation;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = TekRestSharedConstants.TEK_DATE_PATH)
record TekDateController() {

  @GetMapping("/instant")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Allows to get the default java.time.Instant format")
  public Instant getInstant() {
    return Instant.now();
  }

  @GetMapping("/localDate")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Allows to get the default java.time.LocalDate format")
  public LocalDate getLocalDate() {
    return LocalDate.now();
  }

  @GetMapping("/localDateTime")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Allows to get the default java.time.LocalDateTime format")
  public LocalDateTime getLocalDateTime() {
    return LocalDateTime.now();
  }

  @GetMapping("/zonedDatetime")
  @Operation(summary = "Allows to get the default java.time.ZonedDateTime format")
  public ZonedDateTime getZonedDateTime() {
    return ZonedDateTime.now();
  }
}
