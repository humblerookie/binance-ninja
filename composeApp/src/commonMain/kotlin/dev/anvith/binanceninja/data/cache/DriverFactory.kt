package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
  fun createDriver(): SqlDriver
}

internal const val DB_NAME: String = "ninja.db"
