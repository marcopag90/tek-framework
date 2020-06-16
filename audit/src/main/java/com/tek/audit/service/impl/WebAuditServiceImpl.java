package com.tek.audit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.audit.dto.ServletRequestDto;
import com.tek.audit.dto.ServletResponseDto;
import com.tek.audit.model.WebAudit;
import com.tek.audit.model.json.AuditWebRequest;
import com.tek.audit.repository.WebAuditRepository;
import com.tek.audit.service.WebAuditService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Auditing Service to log all Http Requests
 *
 * @author MarcoPagan
 */
@Service
@RequiredArgsConstructor
@ConditionalOnSingleCandidate(WebAuditService.class)
@Slf4j
public class WebAuditServiceImpl implements WebAuditService {

    @NonNull private final WebAuditRepository repository;
    @NonNull private final ObjectMapper objectMapper;

    @Override
    public Optional<WebAudit> read(long id) {
        return repository.findById(id);
    }

    @Override
    public Page<WebAudit> list(Specification<WebAudit> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    public WebAudit logRequest(ServletRequestDto dto) {
        WebAudit webAudit = WebAudit.builder()
            .request(createWebRequest(dto))
            .build();
        return repository.saveAndFlush(webAudit);
    }

    public WebAudit updateRequest(Long id, String body) {
        Optional<WebAudit> optionalWebAudit = repository.findById(id);
        if (optionalWebAudit.isPresent()) {
            WebAudit webAudit = optionalWebAudit.get();
            webAudit.getRequest().setBody(processJsonBody(body));
            return repository.saveAndFlush(webAudit);
        } else {
            log.warn("No [{}] found in repository with id: [{}]", WebAudit.class.getSimpleName(), id);
            return null;
        }
    }

    public WebAudit logResponse(Long id, ServletResponseDto responseDto) {
        Optional<WebAudit> optionalWebAudit = repository.findById(id);
        if (optionalWebAudit.isPresent()) {
            WebAudit webAudit = optionalWebAudit.get();
            webAudit.setStatus(responseDto.getStatus());
            webAudit.setDuration(responseDto.getDuration());
            return repository.saveAndFlush(webAudit);
        } else {
            log.warn("No [{}] found in repository with id: [{}]", WebAudit.class.getSimpleName(), id);
            return null;
        }
    }

    /**
     * Process the request as a JSON
     */
    private AuditWebRequest createWebRequest(ServletRequestDto dto) {
        AuditWebRequest auditWebRequest = AuditWebRequest.builder()
            .issuedAt(Instant.now())
            .method(dto.getMethod())
            .requestUrl(dto.getRequestUrl())
            .requestUri(dto.getRequestUri())
            .headers(dto.getHeaders())
            .parameters(dto.getParameters())
            .queryString(dto.getQueryString())
            .authType(dto.getAuthType())
            .build();

        if (dto.getPrincipal() != null) {
            auditWebRequest.setPrincipalName(dto.getPrincipal().getName());
        }
        return auditWebRequest;
    }

    //TODO check max size with application/octet-stream
    private JsonNode processJsonBody(String body) {
        if (body == null || body.trim().isEmpty()) return null;
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            log.error("Error while trying to create JSON from body: {}", body);
        }
        return null;
    }
}
