package land.mog.kuark

import java.sql.ResultSet

class ResultSetSequence<T>(private val resultSet: ResultSet, private val mapper: (ResultSet) -> T) : AutoCloseable, Sequence<T> {
    override fun close() {
        resultSet.statement.close()
    }

    override fun iterator(): Iterator<T> {
        return iterator {
            while (resultSet.next()) {
                yield(resultSet.run(mapper))
            }
        }
    }
}
