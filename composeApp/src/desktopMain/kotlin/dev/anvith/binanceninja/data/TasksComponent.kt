package dev.anvith.binanceninja.data

import dev.anvith.binanceninja.data.cache.ResetOrdersTask
import dev.anvith.binanceninja.data.remote.RefreshOrdersTask
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface TasksComponent {

    @Provides
    @IntoSet
    fun RefreshOrdersTask.bind(): BackgroundTask = this

    @Provides
    @IntoSet
    fun ResetOrdersTask.bind(): BackgroundTask = this

}