# Kuark

Kuark is a simple SQL library for Kotlin, based on JDBC.

## Example

TODO: More explanation.

```kotlin
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
val dslResult = Kuark.transaction {
    sql {
        select("*") from "test" where { ("id" eq "1") and ("text" eq "test") }
    }.list { Test(it.getInt(1), it.getString(2)) }
}

// Test(id=1, text=first record)
println(singleResult)
// [Test(id=1, text=first record), Test(id=2, text=second record), Test(id=1, text=first record), Test(id=2, text=second record)]
println(listResult)
//[Test(id=1, text=first record), Test(id=1, text=first record)]
println(dslResult)
```

## TODO

- [ ] Add DSL for all of standard queries and attributes
- [ ] Typesafe schema definition
- [ ] Documentation
- [ ] Contribution Guideline
