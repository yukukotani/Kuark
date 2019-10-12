package land.mog.kuark

import java.sql.Connection
import java.sql.DriverManager
import javax.sql.DataSource

interface ConnectionProvider {
    fun getNewConnection(): Connection
}

class DataSourceConnectionProvider(private val dataSource: DataSource) : ConnectionProvider {
    override fun getNewConnection(): Connection {
        return dataSource.connection
    }
}

class DriverManagerConnectionProvider(
    private val url: String,
    private val user: String,
    private val password: String
) : ConnectionProvider {
    override fun getNewConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }

}
