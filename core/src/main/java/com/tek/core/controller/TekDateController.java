package com.tek.core.controller;

import com.tek.core.controller.api.TekResponse;
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
    @ApiOperation(value = "Allows to get the default java.util.Date format")
    public ResponseEntity<TekResponse> getDate() {
        TekResponse body = TekResponse.builder()
            .body(new Date())
            .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/instant")
    @ApiOperation(value = "Allows to get the default java.time.Instant format")
    public ResponseEntity<TekResponse> getInstant() {
        TekResponse body = TekResponse.builder()
            .body(Instant.now())
            .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/localDate")
    @ApiOperation(value = "Allows to get the default java.time.LocalDate format")
    public ResponseEntity<TekResponse> getLocalDate() {
        TekResponse body = TekResponse.builder()
            .body(LocalDate.now())
            .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/localDateTime")
    @ApiOperation(value = "Allows to get the default java.time.LocalDateTime format")
    public ResponseEntity<TekResponse> getLocalDateTime() {
        TekResponse body = TekResponse.builder()
            .body(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/zonedDatetime")
    @ApiOperation(value = "Allows to get the default java.time.ZonedDateTime format")
    public ResponseEntity<TekResponse> getZonedDateTime() {
        TekResponse body = TekResponse.builder()
            .body(ZonedDateTime.now())
            .build();
        return ResponseEntity.ok(body);
    }
}
