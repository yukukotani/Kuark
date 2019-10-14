package land.mog.kuark.builder.dsl

interface Query {
    fun buildQueryString(): String
    fun parameters(): List<Any>
}

class SelectQuery(private val columns: List<String>) : Query {
    var table: String? = null
    var where: SqlExpression? = null

    infix fun from(table: String): SelectQuery {
        this.table = table
        return this
    }

    infix fun where(where: SqlExpressionBuilder.() -> SqlExpression): SelectQuery {
        this.where = SqlExpressionBuilder().run(where)
        return this
    }

    override fun buildQueryString(): String {
        checkNotNull(table) { "You must call from(table) first." }

        return buildString {
            append("SELECT ")
            append(columns.joinToString(separator = ", "))
            append(" FROM $table")
            if (where != null) {
                append(" WHERE ${where!!.buildQueryString()}")
            }
        }
    }

    override fun parameters(): List<Any> {
        return where?.parameters() ?: emptyList()
    }
}
