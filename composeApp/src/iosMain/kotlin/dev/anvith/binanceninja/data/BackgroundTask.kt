package dev.anvith.binanceninja.data

import dev.anvith.binanceninja.domain.RequestExecutor

interface BackgroundTask {
    val taskId: String

    var isScheduled: Boolean

    fun registerHandler():Boolean
    fun schedule(executor: RequestExecutor)
    fun cancel()
}