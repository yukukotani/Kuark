package land.mog.kuark

import java.sql.Connection
import java.sql.ResultSet

class ConnectionScope(val connection: Connection) {
    fun SQL.update(): Int {
        return statement.use { it.executeUpdate() }
    }
    
    fun <T: Any> SQL.single(mapper: (ResultSet) -> T): T {
        return statement.use {
            it.executeQuery().run(mapper)
        }
    }
    
    fun <T : Any> SQL.list(mapper: (ResultSet) -> T): List<T> {
        return sequence(mapper).use {
            it.toList()
        }
    }

    fun <T: Any> SQL.sequence(mapper: (ResultSet) -> T): ResultSetSequence<T> {
        return ResultSetSequence(statement.executeQuery(), mapper)
    }
    
    fun rollback() {
        connection.rollback()
    }
    
    fun commit() {
        connection.commit()
    }
}
