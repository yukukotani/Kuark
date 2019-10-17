package land.mog.kuark.builder

import land.mog.kuark.ConnectionScope
import land.mog.kuark.SQL
import land.mog.kuark.builder.dsl.InsertValueQuery
import land.mog.kuark.builder.dsl.Query
import land.mog.kuark.builder.dsl.SelectQuery

fun ConnectionScope.sql(block: DslQueryBuilder.() -> Query): SQL {
    val query = DslQueryBuilder().run(block)
    val statement = connection.prepareStatement(query.buildQueryString())
        .apply {
            query.parameters().forEachIndexed { index, param -> this.setObject(index + 1, param) }
        }
    return SQL(statement)
}

class DslQueryBuilder {
    fun select(vararg columns: String) = SelectQuery(
        columns = columns.toList()
    )
    
    fun insert(vararg values: Pair<String, Any>) = InsertValueQuery(
        values = values.toMap()
    )
}
