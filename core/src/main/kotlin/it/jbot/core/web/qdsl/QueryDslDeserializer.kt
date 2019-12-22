package it.jbot.core.web.qdsl

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.querydsl.core.types.*
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.Expressions.constant
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.core.types.dsl.PathBuilderValidator
import org.springframework.stereotype.Component

@Component
class QueryDslDeserializer(
    private val mapper: ObjectMapper,
    private val converter: JsonTypeConverter
) {

    /**
     * Create a queryDSL predicate from the specified json
     *
     * @param json        the json predicate
     * @param entityClass the root entity class
     * @return the queryDSL predicate
     */
    fun deserializePredicate(json: String, entityClass: Class<*>): Predicate {
        return try {
            val jsonNode = mapper.readTree(json)
            buildPredicates(jsonNode, entityClass)
        } catch (e: Exception) {
            throw DSLJsonDeserializerException(
                "Unable to deserialize json in queryDSL predicate",
                e
            )
        }
    }

    /**
     * Create a collection of queryDSL order bys from the specified json
     *
     * @param json        the json order bys
     * @param entityClass the root entity class
     * @return the collection of queryDSL order bys
     */
    fun deserializeOrderBys(json: String, entityClass: Class<*>): List<OrderSpecifier<*>> {
        return try {
            val jsonNode = mapper.readTree(json)
            buildOrderBys(jsonNode, entityClass)
        } catch (e: Exception) {
            throw DSLJsonDeserializerException(
                "Unable to deserialize json in queryDSL order bys",
                e
            )
        }
    }

    /**
     * Build all order specifier from the specified JSONObject
     */
    protected fun buildOrderBys(rootNode: JsonNode, entityClass: Class<*>): List<OrderSpecifier<*>> =
        mutableListOf<OrderSpecifier<*>>().apply {
            rootNode.fields().forEach { node ->
                this.addAll(buildPathAndOrderBy(node.key, node.value, entityClass))
            }
        }

    /**
     * Build an order by expression
     */
    protected fun buildPathAndOrderBy(key: String, value: JsonNode, entityClass: Class<*>): List<OrderSpecifier<*>> =
        buildOrderBy(buildPath(key, entityClass), value)

    protected fun buildOrderBy(entityPath: EntityPath<*>, value: JsonNode): List<OrderSpecifier<*>> {

        if (!value.isObject && value.isTextual) {
            @Suppress("UNCHECKED_CAST")
            return listOf(
                OrderSpecifier(convert(value.asText(), Order::class.java), entityPath as Expression<Comparable<*>>)
            )
        }
        throw IllegalArgumentException("The object value have to be a string : $value")
    }

    /**
     * Build all predicates from the specified map
     */
    protected fun buildPredicates(rootNode: JsonNode, entityClass: Class<*>): BooleanExpression {

        val expressions: MutableList<BooleanExpression> = mutableListOf()
        rootNode.fields().forEach { node ->
            expressions.add(buildPredicate(node.key, node.value, entityClass))
        }
        return Expressions.allOf(*expressions.toTypedArray())
    }

    /**
     * Build a predicate with the specified key and object value
     */
    protected fun buildPredicate(
        key: String,
        nodeValue: JsonNode,
        entityClass: Class<*>
    ): BooleanExpression {

        val operator: QueryDslOperator? = getOperator(key)
        operator?.let {
            // the key is an operator
            // it is an array of and or or predicates
            return buildOrAndPredicate(nodeValue, operator, entityClass)
        }
        // build the path
        val entityPath: EntityPath<*> = buildPath(key, entityClass)
        return buildBasePredicate(entityPath, nodeValue)
    }

    protected fun buildOrAndPredicate(
        nodeValue: JsonNode,
        operator: QueryDslOperator,
        entityClass: Class<*>
    ): BooleanExpression {

        val booleanExpressions: MutableList<BooleanExpression> = ArrayList()

        // the key is an operator ($and - $or - ...)
        return if (nodeValue.isArray) {
            nodeValue.elements().forEach { element ->
                if (element.isObject)
                    booleanExpressions.add(buildPredicates(element, entityClass))
                else
                    throw IllegalStateException("expected to have an object instead of $element")
            }
            when (operator) {
                QueryDslOperator.AND -> Expressions.allOf(*booleanExpressions.toTypedArray())
                QueryDslOperator.OR -> Expressions.anyOf(*booleanExpressions.toTypedArray())
                else -> throw IllegalStateException("expected to have an object instead of $operator")
            }
        } else
            throw IllegalStateException("expected to have an array type instead of $nodeValue")
    }

    /**
     * Build base predicate: path - operator - value
     */
    protected fun buildBasePredicate(entityPath: EntityPath<*>, nodeValue: JsonNode): BooleanExpression {

        // add the path to the expressions list
        val expressions: MutableList<Expression<*>> = mutableListOf(entityPath)
        var valueOp = QueryDslOperator.EQ;

        if (nodeValue.isObject) {
            nodeValue.fields().forEach { objectNode ->
                // the key is the operator
                valueOp = QueryDslOperator.getFromJson(objectNode.key)
                when (valueOp) {
                    QueryDslOperator.BETWEEN ->
                        objectNode.value.elements().forEach {
                            expressions.add(createValue(it, entityPath))
                        }
                    QueryDslOperator.IN, QueryDslOperator.NOT_IN ->
                        expressions.add(constant(mutableListOf<Any>().apply {
                            objectNode.value.elements().forEach {
                                this.add(convert(it.asText(), entityPath.type))
                            }
                        }))
                    // simple value
                    else -> expressions.add(createValue(objectNode.value, entityPath))
                }
            }
        }
        // it is a value
        else
            expressions.add(createValue(nodeValue, entityPath))

        return Expressions.predicate(valueOp.dslOperator, *expressions.toTypedArray());
    }

    /**
     * create a queryDSL constant value for the given json node
     */
    protected fun createValue(value: JsonNode, entityPath: EntityPath<*>): Expression<*> =
        constant(convert(value.asText(), entityPath.type))

    /**
     * Cast the json string value into an object of the associated field type
     *
     * @param value the string value
     * @param type  the field type
     * @return the value transformed into the associated field type
     */
    protected fun <T> convert(value: String, type: Class<T>): T = converter.convert(value, type)


    /**
     * Build a queryDSL path from the specified string path
     *
     * @param path        the string path. ex : content.product.packaging
     * @param entityClass the entity root class
     * @return the queryDSL path
     */
    protected fun buildPath(path: String, entityClass: Class<*>): EntityPath<*> {
        val paths = path.split(".").toTypedArray()
        return if (paths.size > 0) {
            var builder = PathBuilder(entityClass, paths[0], PathBuilderValidator.FIELDS)
            for (i in 1 until paths.size) {
                builder = builder.get(paths[i])
            }
            builder
        } else {
            throw java.lang.IllegalArgumentException("The specified path is incorrect : $path")
        }
    }

    /**
     * Get the JSONOperator from the json value
     *
     * @param operator the json operator value
     * @return optional operator
     */
    protected fun getOperator(operator: String): QueryDslOperator? {

        return try {
            QueryDslOperator.getFromJson(operator)
        } catch (ex: IllegalArgumentException) {
            return null
        }
    }

    /**
     * QueryDslToJsonDeserializer exception
     */
    class DSLJsonDeserializerException(message: String?, cause: Throwable?) : RuntimeException(message, cause)
}
