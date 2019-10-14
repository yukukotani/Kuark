package land.mog.kuark.builder

import land.mog.kuark.ConnectionScope
import land.mog.kuark.SQL
import java.sql.PreparedStatement

fun ConnectionScope.raw(block: RawSqlBuilder.() -> String) : SQL {
    val builder = RawSqlBuilder()
    val statement = connection.prepareStatement(builder.run(block))
        .apply {
            builder.params.forEachIndexed { index, param -> this.setObject(index + 1, param.value) }
        }
    return SQL(statement)
}

class RawSqlBuilder {
    val params = mutableListOf<SqlParameter>()

    fun param(value: Any): SqlParameter = SqlParameter(value).also { params.add(it) }
}

inline class SqlParameter(val value: Any) {
    override fun toString(): String = "?"
}
