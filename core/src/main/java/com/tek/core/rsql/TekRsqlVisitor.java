package com.tek.core.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Implementation of {@link RSQLVisitor} to allow the creation of a {@link Specification}.
 *
 * @author MarcoPagan
 */
public class TekRsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

  private final TekRsqlSpecificationBuilder<T> builder;

  public TekRsqlVisitor() {
    builder = new TekRsqlSpecificationBuilder<>();
  }

  @Override
  public Specification<T> visit(AndNode node, Void param) {
    return builder.createSpecification(node);
  }

  @Override
  public Specification<T> visit(OrNode node, Void param) {
    return builder.createSpecification(node);
  }

  @Override
  public Specification<T> visit(ComparisonNode node, Void params) {
    return builder.createSpecification(node);
  }
}
