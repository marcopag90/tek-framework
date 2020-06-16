package com.tek.audit.model.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.*;
import org.apache.commons.collections.MultiMap;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditWebRequest implements Serializable {

    private String method;
    private String requestUrl;
    private String requestUri;
    private HashMap<String, String> headers;
    private Multimap parameters;
    private String queryString;
    private String authType;
    private String principalName;
    private JsonNode body;
    private Instant issuedAt;
}
