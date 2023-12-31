package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            NinjaDatabase.Schema.create(it)
        }
    }
}