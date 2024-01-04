package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.concurrency.DefaultDispatcherProvider
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.data.cache.DataStoreComponent
import dev.anvith.binanceninja.data.remote.ApiComponent
import me.tatarka.inject.annotations.Provides


abstract class SharedAppComponent : DataStoreComponent, ApiComponent, LoggingComponent {

    @Provides
    @AppScope
    fun DefaultDispatcherProvider.bind(): DispatcherProvider = this
}