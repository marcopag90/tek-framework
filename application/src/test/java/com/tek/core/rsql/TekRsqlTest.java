package com.tek.core.rsql;

import com.tek.audit.dto.ServletRequestDto;
import com.tek.audit.dto.ServletResponseDto;
import com.tek.audit.model.WebAudit;
import com.tek.audit.service.WebAuditService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.in;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TekRsqlTest {

    @SuppressWarnings("unused")
    @Autowired
    private WebAuditService service;

    private WebAudit webAuditMockup_1;
    private WebAudit webAuditMockup_2;
    private PageRequest pageRequest;

    @BeforeAll
    public void beforeAll() {
        // ----------------------- First mockup request -----------------------
        ServletRequestDto requestDto_1 = ServletRequestDto.builder()
            .requestUrl("http://localhost:8080/api/web-audit/list")
            .requestUri("/api/web-audit/list")
            .method("GET")
            .build();
        webAuditMockup_1 = service.logRequest(requestDto_1);

        ServletRequestDto requestDto_2 = ServletRequestDto.builder()
            .requestUrl("http://localhost:8080/api/web-audit/list")
            .requestUri("/api/web-audit/list")
            .method("POST")
            .build();
        webAuditMockup_2 = service.logRequest(requestDto_2);

        //----------------------- Second mockup request -----------------------
        ServletResponseDto responseDto_1 = ServletResponseDto.builder()
            .status(200)
            .build();
        webAuditMockup_1 = service.logResponse(webAuditMockup_1.getId(), responseDto_1);

        ServletResponseDto responseDto_2 = ServletResponseDto.builder()
            .status(300)
            .build();
        webAuditMockup_2 = service.logResponse(webAuditMockup_2.getId(), responseDto_2);

        //-------------------- Page request initialization --------------------
        pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
    }

    @AfterAll
    public void afterAll() {
        service.delete(webAuditMockup_1.getId());
        service.delete(webAuditMockup_2.getId());
    }

    @Test
    @Order(1)
    public void testEquality() {
        Specification<WebAudit> specification = createSpecification(
            String.format(
                "id%s%d",
                TekRsqlSearchOperation.EQUAL.toString(), webAuditMockup_1.getId()
            )
        );
        Page<WebAudit> result = service.list(specification, pageRequest);

        assertThat(webAuditMockup_1, is(in(result.getContent())));
        assertThat(webAuditMockup_2, not(in(result.getContent())));
    }

    @Test
    @Order(2)
    public void testNegation() {
        Specification<WebAudit> specification = createSpecification(
            String.format(
                "id%s%d",
                TekRsqlSearchOperation.NOT_EQUAL.toString(), webAuditMockup_1.getId()
            )
        );
        Page<WebAudit> result = service.list(specification, pageRequest);

        assertThat(webAuditMockup_1, not(in(result.getContent())));
        assertThat(webAuditMockup_2, is(in(result.getContent())));
    }

    @Test
    @Order(3)
    public void testGreaterThan() {
        Specification<WebAudit> specification = createSpecification(
            String.format(
                "status%s%d",
                TekRsqlSearchOperation.GREATER_THAN.toString(), webAuditMockup_1.getStatus()
            )
        );
        Page<WebAudit> result = service.list(specification, pageRequest);

        assertThat(webAuditMockup_1, not(in(result.getContent())));
        assertThat(webAuditMockup_2, is(in(result.getContent())));
    }

    @Test
    @Order(4)
    public void testGreaterThanOrEqual() {
        Specification<WebAudit> specification = createSpecification(
            String.format(
                "status%s%d",
                TekRsqlSearchOperation.GREATER_THAN_OR_EQUAL.toString(), 200
            )
        );
        Page<WebAudit> result = service.list(specification, pageRequest);

        assertThat(webAuditMockup_1, is(in(result.getContent())));
        assertThat(webAuditMockup_2, is(in(result.getContent())));
    }

    @Test
    @Order(5)
    public void testLesserThan() {
        Specification<WebAudit> specification = createSpecification(
            String.format(
                "status%s%d",
                TekRsqlSearchOperation.LESS_THAN.toString(), 300
            )
        );
        Page<WebAudit> result = service.list(specification, pageRequest);

        assertThat(webAuditMockup_1, is(in(result.getContent())));
        assertThat(webAuditMockup_2, not(in(result.getContent())));
    }

    @Test
    @Order(6)
    public void testLesserThanOrEqual() {
        Specification<WebAudit> specification = createSpecification(
            String.format(
                "status%s%d",
                TekRsqlSearchOperation.LESS_THAN_OR_EQUAL.toString(), 300
            )
        );
        Page<WebAudit> result = service.list(specification, pageRequest);

        assertThat(webAuditMockup_1, is(in(result.getContent())));
        assertThat(webAuditMockup_2, is(in(result.getContent())));
    }

    //TODO ho bisogno di un campo stringa per testare il like
    @Test
    @Order(7)
    public void testLike() {
    }

    private Specification<WebAudit> createSpecification(String q) {
        Node rootNode = new RSQLParser().parse(q);
        return rootNode.accept(new TekRsqlVisitor<>());
    }
}
