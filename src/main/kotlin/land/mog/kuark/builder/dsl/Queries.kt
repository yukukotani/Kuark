package land.mog.kuark.builder.dsl

interface Query {
    fun buildQueryString(): String
    fun parameters(): List<Any>
}

data class SelectQuery(
    private val table: String? = null,
    private val columns: List<String> = emptyList(),
    private val where: SqlExpression? = null
) : Query {
    infix fun from(table: String): SelectQuery = this.copy(
        table = table
    )

    infix fun where(where: SqlExpressionBuilder.() -> SqlExpression): SelectQuery = this.copy(
        where = SqlExpressionBuilder().run(where)
    )

    override fun buildQueryString(): String {
        checkNotNull(table) { "You must call from(table) first." }

        return buildString {
            append("SELECT ")
            append(columns.joinToString(separator = ", "))
            append(" FROM $table")
            if (where != null) {
                append(" WHERE ${where.buildQueryString()}")
            }
        }
    }

    override fun parameters(): List<Any> {
        return where?.parameters() ?: emptyList()
    }
}

data class InsertValueQuery(
    val table: String? = null,
    val values: Map<String, Any> = emptyMap()
) : Query {

    infix fun into(table: String): InsertValueQuery = this.copy(
        table = table
    )

    override fun buildQueryString(): String {
        checkNotNull(table) { "You must call into(table) first." }
        
        return buildString { 
            append("INSERT INTO $table")
            append("(${values.keys.joinToString(", ")})")
            append(" VALUES (${values.values.joinToString(", ")})")
        }
    }

    override fun parameters(): List<Any> {
        return values.values.toList()
    }
}
