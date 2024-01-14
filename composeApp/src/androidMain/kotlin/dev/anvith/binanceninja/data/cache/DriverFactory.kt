package dev.anvith.binanceninja.data.cache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class DriverFactory(private val context: Context) {
  actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(NinjaDatabase.Schema, context, DB_NAME)
  }
}
