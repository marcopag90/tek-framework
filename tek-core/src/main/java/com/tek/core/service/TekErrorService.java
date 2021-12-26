package com.tek.core.service;

import com.tek.core.dto.TekErrorDto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Service
public record TekErrorService(TekRestMessage restMessage) {

  public TekErrorDto createErrorResponse(WebRequest request) {
    return new TekErrorDto(
        restMessage.ko(),
        ((ServletWebRequest) request).getRequest().getServletPath()
    );
  }
}
