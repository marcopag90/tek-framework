package com.tek.audit.controller;

import com.tek.audit.model.WebAudit;
import com.tek.audit.service.WebAuditService;
import com.tek.core.conf.swagger.ApiPageable;
import com.tek.core.controller.TekMapping;
import com.tek.core.controller.api.TekPage;
import com.tek.core.rsql.TekRsqlVisitor;
import com.tek.core.service.TekRestMessage;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tek.audit.TekAuditConstant.TEK_WEB_AUDIT_PATH;

/**
 * API to rsql the {@link com.tek.audit.model.WebAudit} table
 *
 * @author MarcoPagan
 */
@RestController
@RequestMapping(
    path = TEK_WEB_AUDIT_PATH,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class TekWebAuditController {

  @NonNull private final WebAuditService service;
  @NonNull private final TekRestMessage tekRestMessage;

  @SuppressWarnings("unused")
  @GetMapping(TekMapping.LIST)
  @ApiPageable
  public ResponseEntity<TekPage<WebAudit>> list(
      @RequestParam(value = "q") String q,
      @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    Node rootNode = new RSQLParser().parse(q);
    Specification<WebAudit> spec = rootNode.accept(new TekRsqlVisitor<>());
    TekPage<WebAudit> result = new TekPage<>(service.list(spec, pageable));
    return ResponseEntity.ok(result);
  }

}
