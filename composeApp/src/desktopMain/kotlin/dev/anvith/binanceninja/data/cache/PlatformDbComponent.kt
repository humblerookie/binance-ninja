package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver
import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Provides

internal actual interface PlatformDbComponent {
    @Provides
    @AppScope
    fun driver(driverFactory: DriverFactory): SqlDriver = driverFactory.createDriver()

}