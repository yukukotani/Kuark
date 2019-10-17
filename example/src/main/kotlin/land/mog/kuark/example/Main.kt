package land.mog.kuark.example

import land.mog.kuark.Kuark
import land.mog.kuark.builder.raw
import land.mog.kuark.builder.sql

fun main() {
// Connect to DB
    Kuark.connect("jdbc:sqlite:file:example.db")

// Call raw query.
    Kuark.transaction {
        raw { "CREATE TABLE IF NOT EXISTS test(id integer, text text)" }.update()
    }

// Use `param(value)` to pass escaped parameter to PreparedStatement.
    Kuark.transaction {
        val id = 1
        val text = "first record"
        raw { "INSERT INTO test VALUES(${param(id)}, ${param(text)})" }.update()
        raw { "INSERT INTO test VALUES(${param(2)}, ${param("second record")})" }.update()
    }

// Use `SQL.single(transform)` to get a single result from ResultSet.
    val singleResult = Kuark.transaction {
        raw { "SELECT * from test WHERE id = ${param(1)}" }.single { Test(it.getInt(1), it.getString(2)) }
    }
// Use `SQL.list(transform)` to get a list result from ResultSet.
    val listResult = Kuark.transaction {
        raw { "SELECT * from test" }.list { Test(it.getInt(1), it.getString(2)) }
    }
// You can also get a raw ResultSet Sequence for flexible use.
// NOTE: You must close ResultSet by yourself.
    Kuark.transaction {
        raw { "SELECT * from test" }.sequence { Test(it.getInt(1), it.getString(2)) }.use {
            it.take(3).forEach {
                // take first three records
            }
        }
    }

// Also we have simple DSL to build typesafe query.
    Kuark.transaction {
        sql {
            insert("id" to 3, "text" to "third record") into "test"
        }
    }
    val dslResult = Kuark.transaction {
        sql{
            select("*") from "test" where { ("id" eq "1") and ("text" eq "first record") }
        }.list { Test(it.getInt(1), it.getString(2)) }
    }

    println(singleResult)
    println(listResult)
    println(dslResult)
}

data class Test(
    val id: Int,
    val text: String
)
