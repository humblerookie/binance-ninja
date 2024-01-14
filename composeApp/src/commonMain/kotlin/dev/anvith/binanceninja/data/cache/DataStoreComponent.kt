package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.db.SqlDriver
import com.russhwolf.settings.Settings
import me.tatarka.inject.annotations.Provides

internal interface DataStoreComponent : PlatformDbComponent {

  @Provides fun settings() = Settings()

  @Provides fun userData(settings: Settings) = UserDataStore(settings)

  @Provides fun database(sqlDriver: SqlDriver): NinjaDatabase = NinjaDatabase(sqlDriver)

  @Provides fun filterQueries(db: NinjaDatabase): FilterQueries = db.filterQueries

  @Provides fun currencyQueries(db: NinjaDatabase): CurrencyQueries = db.currencyQueries

  @Provides fun orderQueries(db: NinjaDatabase): OrdersQueries = db.ordersQueries
}
