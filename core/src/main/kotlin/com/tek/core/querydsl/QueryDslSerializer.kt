package com.tek.core.querydsl

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.*
import org.springframework.stereotype.Component

@Component
class QueryDslSerializer(
    private val mapper: ObjectMapper
) : Visitor<Any, Unit> {

    /**
     * Serialize the specified list of orderBys
     *
     * @request orderBys collection of orderBys
     * @return the json of the queryDSl orders
     */
    fun serializeAsNode(orderBys: List<OrderSpecifier<*>>): JsonNode? {
        val node: ObjectNode = mapper.createObjectNode()
        for (orderBy in orderBys) {
            val key = orderBy.target.accept(this, null)
            node.put(key.toString(), orderBy.order.toString())
        }
        return node
    }

    /**
     * Serialize the specified expression in the json object representation
     *
     * @request expression the queryDSL expression
     * @return a expressions object (the json object representation)
     */
    fun serializeAsNode(expression: Expression<*>): JsonNode? {

        return if (expression is BooleanBuilder) {
            if (expression.value == null)
                null
            else
                mapper.valueToTree(expression.accept(this, null))
        } else
            mapper.valueToTree(expression.accept(this, null))
    }


    protected fun asJsonObject(key: String, value: Any): ObjectNode =
        mapper.createObjectNode().set(key, mapper.valueToTree(value)) as ObjectNode

    protected fun asJsonObject(op: QueryDslOperator, value: Any): ObjectNode =
        asJsonObject(op.json, value)

    private fun asJsonKey(expr: Operation<*>, index: Int): String = asJsonValue(expr, index) as String

    private fun asJsonValue(expr: Operation<*>, index: Int): Any = expr.getArg(index).accept(this, null)!!

    override fun visit(expr: Constant<*>, context: Unit?): Any? {
        return if (Enum::class.java.isAssignableFrom(expr.type)) {
            (expr.constant as Enum<*>).name
        } else {
            expr.constant
        }
    }

    override fun visit(expr: Operation<*>, context: Unit?): Any? {

        return when (val op = expr.operator) {
            Ops.EQ -> asJsonObject(asJsonKey(expr, 0), asJsonValue(expr, 1))
            Ops.STRING_IS_EMPTY -> asJsonObject(asJsonKey(expr, 0), "")
            Ops.AND -> asNodeAND(expr)
            Ops.OR -> asJsonObject(
                QueryDslOperator.getFromDsl(op), mapper.createArrayNode()
                    .add(serializeAsNode(expr.getArg(0)))
                    .add(serializeAsNode(expr.getArg(1)))
            )
            Ops.BETWEEN -> asJsonObject(
                asJsonKey(expr, 0), asJsonObject(
                    QueryDslOperator.getFromDsl(op), mapper.createArrayNode()
                        .add(serializeAsNode(expr.getArg(1)))
                        .add(serializeAsNode(expr.getArg(2)))
                )
            )
            Ops.IN, Ops.NOT_IN ->
                if (Collection::class.java.isAssignableFrom(expr.getArg(1).type)) {
                    val values = (expr.getArg(1) as Constant<Collection<out Any>>).constant
                    asJsonObject(
                        asJsonKey(expr, 0),
                        asJsonObject(QueryDslOperator.getFromDsl(op), values.toTypedArray())
                    )
                } else throw UnsupportedOperationException("Illegal operation $expr")
            Ops.NE,
            Ops.STARTS_WITH,
            Ops.ENDS_WITH,
            Ops.STRING_CONTAINS,
            Ops.LIKE,
            Ops.LT, Ops.GT,
            Ops.LOE, Ops.GOE -> asJsonObject(
                asJsonKey(expr, 0),
                asJsonObject(QueryDslOperator.getFromDsl(op), asJsonValue(expr, 1))
            )
            else -> throw UnsupportedOperationException("Illegal operation $expr")
        }

    }

    private fun asNodeAND(expr: Operation<*>): JsonNode {
        val lhs = serializeAsNode(expr.getArg(0)) as ObjectNode
        val rhs = serializeAsNode(expr.getArg(1)) as ObjectNode
        return lhs.setAll(rhs)
    }

    override fun visit(expr: Path<*>?, context: Unit?): Any? {
        val metadata = expr!!.metadata
        return if (metadata.parent != null) {
            if (metadata.pathType == PathType.COLLECTION_ANY) {
                visit(metadata.parent, context)
            } else {
                val rv = getKeyForPath(metadata)
                visit(metadata.parent, context).toString() + "." + rv
            }
        } else getKeyForPath(metadata)
    }

    protected fun getKeyForPath(metadata: PathMetadata): String =
        metadata.element.toString()

    override fun visit(p0: SubQueryExpression<*>?, p1: Unit?): Any? {
        throw UnsupportedOperationException()
    }

    override fun visit(p0: TemplateExpression<*>?, p1: Unit?): Any? {
        throw UnsupportedOperationException()
    }

    override fun visit(p0: ParamExpression<*>?, p1: Unit?): Any? {
        throw UnsupportedOperationException()
    }

    override fun visit(expr: FactoryExpression<*>?, context: Unit?): Any? {
        throw UnsupportedOperationException()
    }
}
