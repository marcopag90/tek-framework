package com.tek.core.service;

import com.tek.core.controller.api.TekErrorResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Service
@RequiredArgsConstructor
public class TekErrorService {

  @NonNull
  private final TekRestMessage restMessage;

  public TekErrorResponse createErrorResponse(WebRequest request) {
    return TekErrorResponse.builder()
        .message(restMessage.ko())
        .path(((ServletWebRequest) request).getRequest().getServletPath())
        .build();
  }
}
