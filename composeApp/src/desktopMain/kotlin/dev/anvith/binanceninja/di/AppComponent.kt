package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.data.cache.CurrencyRepository
import dev.anvith.binanceninja.data.cache.FilterRepository
import me.tatarka.inject.annotations.Component


@Component
@AppScope
abstract class AppComponent() : SharedAppComponent() {

    abstract val filterRepository: FilterRepository
    abstract val currencyRepository: CurrencyRepository
    abstract val initializers: Set<Initializer>


}