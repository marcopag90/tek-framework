package com.tek.core.service;

import com.tek.core.controller.api.TekErrorResponse;
import com.tek.core.service.impl.TekRestMessageServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Service
@RequiredArgsConstructor
public class TekErrorService {

    @NonNull
    private final TekRestMessageServiceImpl sharedRestMessage;

    public TekErrorResponse createErrorResponse(Exception ex, WebRequest request) {
        String message = sharedRestMessage.ko();
        String path = ((ServletWebRequest) request).getRequest().getServletPath();
        return TekErrorResponse.builder()
            .message(message)
            .path(path)
            .build();
    }
}
