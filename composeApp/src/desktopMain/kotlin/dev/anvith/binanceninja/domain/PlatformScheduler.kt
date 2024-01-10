package dev.anvith.binanceninja.domain

import dev.anvith.binanceninja.data.BackgroundTask
import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Inject


@Inject
@AppScope
actual class PlatformScheduler(
    private val tasks: Set<BackgroundTask>,
) {


    actual fun schedule(executor: RequestExecutor) {
        tasks.forEach { it.schedule(executor) }

    }

    actual fun cancel() {
        tasks.forEach(BackgroundTask::cancel)
    }

}