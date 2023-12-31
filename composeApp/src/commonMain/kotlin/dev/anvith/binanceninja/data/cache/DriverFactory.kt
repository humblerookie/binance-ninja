package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver
import me.tatarka.inject.annotations.Inject

expect class DriverFactory {
    fun createDriver(): SqlDriver

}
internal  const val  DB_NAME: String = "ninja.db"