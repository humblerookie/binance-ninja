package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.data.cache.DataStoreComponent
import dev.anvith.binanceninja.data.remote.ApiComponent
import dev.anvith.binanceninja.domain.PeriodicScheduler
import dev.anvith.binanceninja.domain.RequestExecutor
import me.tatarka.inject.annotations.Provides


abstract class SharedAppComponent : DataStoreComponent, ApiComponent, LoggingComponent,
    SchedulerComponent, MiscComponent {



    @Provides
    @AppScope
    fun PeriodicScheduler.requestExecutor(): RequestExecutor = this
}