package land.mog.kuark.builder.dsl

interface SqlExpression : Query

class EqualsExpression(private val column: String, private val value: Any) : SqlExpression {
    override fun buildQueryString(): String {
        return "$column = ?"
    }

    override fun parameters(): List<Any> {
        return listOf(value)
    }
}

class AndExpression(private val left: SqlExpression, private val right: SqlExpression) : SqlExpression {
    override fun buildQueryString(): String {
        return "(${left.buildQueryString()} AND ${right.buildQueryString()})"
    }

    override fun parameters(): List<Any> {
        return left.parameters() + right.parameters()
    }
}

class OrExpression(private val left: SqlExpression, private val right: SqlExpression) : SqlExpression {
    override fun buildQueryString(): String {
        return "(${left.buildQueryString()} OR ${right.buildQueryString()})"
    }

    override fun parameters(): List<Any> {
        return left.parameters() + right.parameters()
    }
}

class SqlExpressionBuilder {
    infix fun String.eq(value: String): EqualsExpression = EqualsExpression(this, value)
    infix fun SqlExpression.and(expr: SqlExpression) = AndExpression(this, expr)
    infix fun SqlExpression.or(expr: SqlExpression) = OrExpression(this, expr)
}
