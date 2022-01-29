package com.tek.audit.service;

import com.tek.audit.dto.ServletRequestDto;
import com.tek.audit.dto.ServletResponseDto;
import com.tek.audit.model.WebAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface WebAuditService {

  // -------------------------- CRUD ------------------------------------------

  Optional<WebAudit> read(long id);

  Page<WebAudit> list(Specification<WebAudit> spec, Pageable pageable);

  void delete(long id);

  // -------------------------- BUSINESS --------------------------------------

  WebAudit logRequest(ServletRequestDto dto);

  WebAudit logResponse(Long id, ServletResponseDto responseDto);

  WebAudit updateRequest(Long id, String body);
}



