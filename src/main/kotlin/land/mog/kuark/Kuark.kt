package land.mog.kuark

import javax.sql.DataSource

object Kuark {
    private var connectionProvider: ConnectionProvider? = null
    
    fun connect(dataSource: DataSource): ConnectionProvider {
        return DataSourceConnectionProvider(dataSource).also { 
            this.connectionProvider = it
        }
    }
    
    fun connect(url: String, user: String = "", password: String = ""): ConnectionProvider {
        return DriverManagerConnectionProvider(url, user, password).also {
            this.connectionProvider = it
        }
    }

    fun <T> transaction(
        connectionProvider: ConnectionProvider = this.connectionProvider ?: throw IllegalStateException("You must call Kuark.connect() first!"),
        block: ConnectionScope.() -> T
    ): T {
        val connection = connectionProvider.getNewConnection().apply { 
            autoCommit = false
        }
        
        return connection.use {
            val scope = ConnectionScope(connection)
            scope.run(block).also { connection.commit() }
        }
    }
}
