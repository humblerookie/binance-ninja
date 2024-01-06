package dev.anvith.binanceninja.domain

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.Constants
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.darwin.dispatch_io_t

@Inject
actual class PlatformScheduler(
    private val periodicScheduler: PeriodicScheduler,
    private val dispatcherProvider: DispatcherProvider,
) {
    private val taskId = "dev.anvith.binanceninja.FilterOrders.refresh"

    actual fun schedule() {
        registerHandler()
        scheduleRequest()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun scheduleRequest() {
        try {
            val request = BGAppRefreshTaskRequest(taskId)
            request.earliestBeginDate = getNextScheduledTime()
            BGTaskScheduler.sharedScheduler.submitTaskRequest(request, null)
        } catch (e: Exception) {
            logE("Error submitting task request $e")
        }
    }

    private fun getNextScheduledTime(): NSDate {
        val currentDate = NSDate()
        val calendar = NSCalendar.currentCalendar
        val components = NSDateComponents().apply {
            minute = Constants.INTERVAL_MINUTES
        }
        return calendar.dateByAddingComponents(components, currentDate, 0u)!!
    }

    private fun registerHandler() {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            taskId,
            dispatch_io_t()
        ) { task ->
            val scope =
                CoroutineScope(SupervisorJob() + dispatcherProvider.io() + CoroutineExceptionHandler { _, exception ->
                    logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
                })
            task?.expirationHandler = { scope.cancel() }
            scheduleRequest()

            scope.launch {
                var count = 0
                var success = false
                while (count <= Constants.RETRIES && !success) {
                    success = periodicScheduler.executeRequests()
                    count++
                }
                task?.setTaskCompletedWithSuccess(success)
            }

        }
    }

    actual fun cancel() {
        BGTaskScheduler.sharedScheduler.cancelTaskRequestWithIdentifier(taskId)
    }

}