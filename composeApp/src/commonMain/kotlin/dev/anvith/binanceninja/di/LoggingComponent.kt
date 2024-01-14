package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.core.LoggingInitializer
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface LoggingComponent {

  @Provides @IntoSet fun LoggingInitializer.bind(): Initializer = this
}
