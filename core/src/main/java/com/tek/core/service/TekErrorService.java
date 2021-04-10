package com.tek.core.service;

import com.tek.core.dto.TekErrorDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Service
@RequiredArgsConstructor
public class TekErrorService {

  @NonNull private final TekRestMessage restMessage;

  public TekErrorDto createErrorResponse(WebRequest request) {
    return TekErrorDto.builder()
        .message(restMessage.ko())
        .path(((ServletWebRequest) request).getRequest().getServletPath())
        .build();
  }
}
