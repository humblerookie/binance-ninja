package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.concurrency.DefaultDispatcherProvider
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import me.tatarka.inject.annotations.Provides

actual interface MiscComponent{

    @Provides
    @AppScope
    fun DefaultDispatcherProvider.bind(): DispatcherProvider = this
}