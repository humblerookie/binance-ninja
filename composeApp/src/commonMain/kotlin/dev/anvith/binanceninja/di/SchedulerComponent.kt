package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.domain.PeriodicScheduler
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SchedulerComponent {

    @Provides
    @IntoSet
    fun PeriodicScheduler.bind(): Initializer = this


}