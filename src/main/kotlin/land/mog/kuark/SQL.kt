package land.mog.kuark

import java.sql.PreparedStatement

inline class SQL(val statement: PreparedStatement) : AutoCloseable {

    val isClosed: Boolean get() = statement.isClosed

    override fun close() {
        statement.close()
    }
}
