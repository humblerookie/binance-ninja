package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver
import me.tatarka.inject.annotations.Provides


internal interface DbComponent : PlatformDbComponent {

    @Provides
    fun database(sqlDriver: SqlDriver): NinjaDatabase = NinjaDatabase(sqlDriver)

    @Provides
    fun filterQueries(db: NinjaDatabase): FilterQueries = db.filterQueries
}