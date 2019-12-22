package it.jbot.core.web.qdsl

import com.querydsl.core.types.Operator
import com.querydsl.core.types.Ops

/**
 * This class represents all queryDSL operators and the associated Json operator.
 *
 *
 * It permits to link the json operator to the queryDSL operator.
 */
enum class QueryDslOperator(
    /**
     * the associated json operator
     */
    val json: String,
    /**
     * the associated queryDSL operator
     */
    val dslOperator: Operator
) {
    EQ("\$eq", Ops.EQ),
    NE("\$ne", Ops.NE),
    LT("\$lt", Ops.LT),
    GT("\$gt", Ops.GT),
    LOE("\$lte", Ops.LOE),
    GOE("\$gte", Ops.GOE),
    LIKE("\$like", Ops.LIKE),
    STARTS_WITH("\$start", Ops.STARTS_WITH),
    ENDS_WITH("\$end", Ops.ENDS_WITH),
    AND("\$and", Ops.AND),
    OR("\$or", Ops.OR),
    BETWEEN("\$between", Ops.BETWEEN),
    IN("\$in", Ops.IN),
    NOT_IN("\$nin", Ops.NOT_IN),
    STRING_CONTAINS("\$contains", Ops.STRING_CONTAINS);

    override fun toString(): String = json

    companion object {

        /**
         * Get the expression operator of the specified queryDSL operator
         *
         * @param op the queryDSL operator
         * @return the expression operator
         */
        fun getFromDsl(op: Operator): QueryDslOperator {
            for (queryDslOperator in values()) {
                if (queryDslOperator.dslOperator == op) {
                    return queryDslOperator
                }
            }
            throw IllegalArgumentException("Illegal operator $op")
        }

        /**
         * get the expression operator from the json
         *
         * @param jsonValue the json value of the operator
         * @return the expression operator
         */
        fun getFromJson(jsonValue: String): QueryDslOperator {
            for (expressionOperator in values()) {
                if (expressionOperator.json == jsonValue) {
                    return expressionOperator
                }
            }
            throw IllegalArgumentException("QueryDSL operator $jsonValue unknown.")
        }
    }
}
