package dev.anvith.binanceninja.domain

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.data.BackgroundTask
import me.tatarka.inject.annotations.Inject

@Inject
actual class PlatformScheduler(private val tasks: Set<BackgroundTask>) : Initializer {

  override fun initialize() {
    tasks.forEach(BackgroundTask::registerHandler)
  }

  actual fun schedule(executor: RequestExecutor) {
    tasks.forEach { it.schedule(executor) }
  }

  actual fun cancel() {
    tasks.forEach(BackgroundTask::cancel)
  }
}
