package com.tek.core.rsql;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.tek.core.rsql.model.Developer;
import com.tek.core.rsql.repository.DeveloperRepository;
import cz.jirutka.rsql.parser.RSQLParser;
import io.swagger.models.auth.In;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(
    properties = {
        "spring.jpa.properties.hibernate.format_sql=true",
        "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
    }
)
@SuppressWarnings("squid:S5976")
class RsqlTest {

  @Autowired
  private DeveloperRepository repository;

  private final Developer marco = Developer.builder()
      .firstName("Marco")
      .lastName("Pagan")
      .createdAt(Instant.now())
      .birthDate(LocalDate.of(1990, Month.MAY, 23))
      .salary(30500.20)
      .lastPayment(new BigDecimal("1800.00"))
      .build();

  private final Developer anthony = Developer.builder()
      .firstName("Anthony")
      .lastName("Ferranti")
      .createdAt(Instant.now())
      .salary(30500.30)
      .lastPayment(new BigDecimal("1800.018"))
      .birthDate(LocalDate.of(1990, Month.NOVEMBER, 6))
      .build();

  @BeforeAll
  void seedDatabase() {
    val developers = new ArrayList<Developer>();
    developers.add(marco);
    developers.add(anthony);
    repository.saveAll(developers);
  }

  //---------------------------------------EQUAL----------------------------------------------------

  @Test
  void equal_with_String() {
    val rootNode = new RSQLParser().parse("firstName==Marco;lastName==Pagan");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(marco)),
        () -> assertThat(result, not(hasItems(anthony)))
    );
  }

  @Test
  void equal_with_Integer() {
    val rootNode = new RSQLParser().parse("id==1");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(marco)),
        () -> assertThat(result, not(hasItems(anthony)))
    );
  }

  @Test
  void equal_with_Double() {
    val rootNode = new RSQLParser().parse("salary==30500.20");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(marco)),
        () -> assertThat(result, not(hasItems(anthony)))
    );
  }

  @Test
  void equal_with_BigDecimal() {
    val rootNode = new RSQLParser().parse("lastPayment==1800.018");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(anthony)),
        () -> assertThat(result, not(hasItems(marco)))
    );
  }

  @Test
  void equal_with_LocalDate() {
    val rootNode = new RSQLParser().parse("birthDate==1990-05-23");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(marco)),
        () -> assertThat(result, not(hasItems(anthony)))
    );
  }

  //--------------------------------------NOT EQUAL-------------------------------------------------

  @Test
  void notEqual_with_String() {
    val rootNode = new RSQLParser().parse("firstName!=Marco");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, not(hasItems(marco))),
        () -> assertThat(result, hasItems(anthony))
    );
  }

  @Test
  void notEqual_with_Integer() {
    val rootNode = new RSQLParser().parse("id!=1");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, not(hasItems(marco))),
        () -> assertThat(result, hasItems(anthony))
    );
  }

  @Test
  void notEqual_with_Double() {
    val rootNode = new RSQLParser().parse("salary!=30500.20");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(anthony)),
        () -> assertThat(result, not(hasItems(marco)))
    );
  }

  @Test
  void notEqual_with_BigDecimal() {
    val rootNode = new RSQLParser().parse("lastPayment!=1800.018");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(marco)),
        () -> assertThat(result, not(hasItems(anthony)))
    );
  }

  @Test
  void notEqual_with_Instant() {
    val query = String.format("createdAt!=%s", Instant.now());
    val rootNode = new RSQLParser().parse(query);
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    assertThat(result, hasItems(marco, anthony));
  }

  //---------------------------------------LIKE-----------------------------------------------------

  @Test
  void likeStarts_with_String() {
    val rootNode = new RSQLParser().parse("lastName==Pagan;firstName==M*");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, not(hasItems(anthony))),
        () -> assertThat(result, hasItems(marco))
    );
  }

  @Test
  void notLikeStarts_with_String() {
    val rootNode = new RSQLParser().parse("lastName==Ferranti;firstName!=M*");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    Assertions.assertAll(
        () -> assertThat(result, hasItems(anthony)),
        () -> assertThat(result, not(hasItems(marco)))
    );
  }

  //--------------------------------------LESS THAN-------------------------------------------------

  @Test
  void lessThan_with_LocalDate() {
    val rootNode = new RSQLParser().parse("birthDate<2020-01-01");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    assertThat(result, hasItems(marco, anthony));
  }

  @Test
  void lessThan_with_Instant() {
    val query = String.format("createdAt<%s", Instant.now());
    val rootNode = new RSQLParser().parse(query);
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    assertThat(result, hasItems(marco, anthony));
  }

  //--------------------------------------GREATER THAN----------------------------------------------

  @Test
  void greaterThan_with_LocalDate() {
    val rootNode = new RSQLParser().parse("birthDate>1990-01-01");
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    assertThat(result, hasItems(marco, anthony));
  }

  @Test
  void greaterThan_with_Instant() {
    val query = String.format("createdAt>%s", Instant.now());
    val rootNode = new RSQLParser().parse(query);
    Specification<Developer> spec = rootNode.accept(new TekRsqlVisitor<>());
    List<Developer> result = repository.findAll(spec);
    assertThat(result, not(hasItems(marco, anthony)));
  }
}






















