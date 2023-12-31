package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(NinjaDatabase.Schema, DB_NAME)
    }
}