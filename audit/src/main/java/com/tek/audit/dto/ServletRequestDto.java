package com.tek.audit.dto;

import com.google.common.collect.Multimap;
import lombok.*;

import java.security.Principal;
import java.util.HashMap;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServletRequestDto {

    private String method;
    private String requestUrl;
    private String requestUri;
    private HashMap<String, String> headers;
    private Multimap<String, String> parameters;
    private String queryString;
    private String authType;
    private Principal principal;
}
