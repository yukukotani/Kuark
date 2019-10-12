package land.mog.kuark.example

import land.mog.kuark.Kuark
import land.mog.kuark.raw

fun main() {
    val dbFile = createTempFile()
    dbFile.deleteOnExit()
    Kuark.connect("jdbc:sqlite:file:${dbFile.absolutePath}")

    Kuark.transaction {
        raw { "CREATE TABLE IF NOT EXISTS test(id integer, text text)" }.update()
    }
    Kuark.transaction {
        val id = 1
        val text = "test"
        raw { "INSERT INTO test VALUES(${param(id)}, ${param(text)})" }.update()
    }
    val result = Kuark.transaction { 
        raw { "SELECT * from test" }.list { Test(it.getInt(1), it.getString(2)) }
    }

    println(result)
}

data class Test(
    val id: Int,
    val text: String
)
