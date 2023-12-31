package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.concurrency.DefaultDispatcherProvider
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.data.cache.DbComponent
import me.tatarka.inject.annotations.Provides


abstract class SharedAppComponent : DbComponent {

    @Provides
    @AppScope
    fun DefaultDispatcherProvider.bind(): DispatcherProvider = this
}