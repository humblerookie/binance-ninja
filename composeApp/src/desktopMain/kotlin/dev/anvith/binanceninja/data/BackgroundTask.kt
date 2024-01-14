package dev.anvith.binanceninja.data

import dev.anvith.binanceninja.domain.RequestExecutor

interface BackgroundTask {
  fun schedule(executor: RequestExecutor)

  fun cancel()
}
