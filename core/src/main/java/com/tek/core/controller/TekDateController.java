package com.tek.core.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.tek.core.TekCoreConstant.TEK_DATE_PATH;

@RestController
@RequestMapping(
    path = TEK_DATE_PATH,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class TekDateController {

  @GetMapping("/date")
  @ApiOperation(
      value = "Allows to get the default java.util.Date format"
  )
  public ResponseEntity<Date> getDate() {
    return ResponseEntity.ok(new Date());
  }

  @GetMapping("/instant")
  @ApiOperation(value = "Allows to get the default java.time.Instant format")
  public ResponseEntity<Instant> getInstant() {
    return ResponseEntity.ok(Instant.now());
  }

  @GetMapping("/localDate")
  @ApiOperation(value = "Allows to get the default java.time.LocalDate format")
  public ResponseEntity<LocalDate> getLocalDate() {
    return ResponseEntity.ok(LocalDate.now());
  }

  @GetMapping("/localDateTime")
  @ApiOperation(value = "Allows to get the default java.time.LocalDateTime format")
  public ResponseEntity<LocalDateTime> getLocalDateTime() {
    return ResponseEntity.ok(LocalDateTime.now());
  }

  @GetMapping("/zonedDatetime")
  @ApiOperation(value = "Allows to get the default java.time.ZonedDateTime format")
  public ResponseEntity<ZonedDateTime> getZonedDateTime() {
    return ResponseEntity.ok(ZonedDateTime.now());
  }
}