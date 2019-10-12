package land.mog.kuark.builder

import java.sql.PreparedStatement

class RawSqlBuilder {
    val params = mutableListOf<SqlParameter>()

    fun param(value: Any): SqlParameter = SqlParameter(value).also { params.add(it) }
}

inline class SQL(val statement: PreparedStatement) : AutoCloseable {

    val isClosed: Boolean get() = statement.isClosed

    override fun close() {
        statement.close()
    }
}

inline class SqlParameter(val value: Any) {
    override fun toString(): String = "?"

}
