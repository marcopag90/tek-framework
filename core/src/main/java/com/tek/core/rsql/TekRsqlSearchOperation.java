package com.tek.core.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import lombok.Getter;

@Getter
public enum TekRsqlSearchOperation {

  EQUAL(RSQLOperators.EQUAL),
  NOT_EQUAL(RSQLOperators.NOT_EQUAL),
  GREATER_THAN(RSQLOperators.GREATER_THAN),
  GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL),
  LESS_THAN(RSQLOperators.LESS_THAN),
  LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL),
  IN(RSQLOperators.IN),
  NOT_IN(RSQLOperators.NOT_IN);

  private final ComparisonOperator operator;

  TekRsqlSearchOperation(ComparisonOperator operator) {
    this.operator = operator;
  }

  @Override
  public String toString() {
    return this.operator.getSymbol();
  }

  public static TekRsqlSearchOperation getSimpleOperator(ComparisonOperator operator) {
    for (TekRsqlSearchOperation operation : values()) {
      if (operation.getOperator() == operator) {
        return operation;
      }
    }
    return null;
  }
}
