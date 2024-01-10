package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.core.concurrency.DefaultDispatcherProvider
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.data.TasksComponent
import dev.anvith.binanceninja.domain.PlatformScheduler
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

actual interface MiscComponent : TasksComponent {

    @Provides
    @IntoSet
    fun PlatformScheduler.bind(): Initializer = this

    @Provides
    @AppScope
    fun DefaultDispatcherProvider.bind(): DispatcherProvider = this
}