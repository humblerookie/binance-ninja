package dev.anvith.binanceninja.di

import android.content.Context
import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.data.cache.CurrencyRepository
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.domain.PeriodicScheduler
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides


@Component
@AppScope
abstract class AppComponent(
    @get:Provides val context: Context
) : SharedAppComponent(){
    abstract val filterRepository: FilterRepository
    abstract val currencyRepository: CurrencyRepository
    abstract val scheduler: PeriodicScheduler
    abstract val initializers: Set<Initializer>
}