package dev.anvith.binanceninja.domain

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logD
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.Constants
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.get
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.value
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
import platform.Foundation.NSError

@Inject
actual class PlatformScheduler(
    private val dispatcherProvider: DispatcherProvider
) : Initializer {
    private val taskId = "dev.anvith.binanceninja.FilterOrders.refresh"
    private var executor: RequestExecutor? = null
    private var isScheduled = false
    override fun initialize() {
        registerHandler()
    }

    actual fun schedule(executor: RequestExecutor) {
        this.executor = executor
        scheduleRequest()
    }


    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun scheduleRequest(executeNow:Boolean =false) {
        if (!isScheduled) {
            try {
                logD("Scheduling background task")
                val request = BGAppRefreshTaskRequest(taskId)
                request.earliestBeginDate = getNextScheduledTime(executeNow)
                logD("Scheduling background task at ${request.earliestBeginDate}")
                memScoped {
                    val error = interpretCPointer<ObjCObjectVar<NSError?>>(alloc(1,1).rawPtr)!!
                    isScheduled = BGTaskScheduler.sharedScheduler.submitTaskRequest(request, error)
                    logD("Submitted background task $isScheduled, ${error[0].value}")
                }

            } catch (e: Exception) {
                logE("Error submitting task request $e")
            }
        }
    }

    private fun getNextScheduledTime(executeNow: Boolean): NSDate {
        val currentDate = NSDate()
        val calendar = NSCalendar.currentCalendar
        val components = NSDateComponents().apply {
            if (executeNow) {
                second = 5
            } else {
                minute = Constants.INTERVAL_MINUTES
            }
        }
        return calendar.dateByAddingComponents(components, currentDate, 0u)!!
    }

    private fun registerHandler() {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            taskId,
            null,
        ) { task ->
            logD("Running background task")
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
                    success = executor?.executeRequests()?:false
                    count++
                }
                task?.setTaskCompletedWithSuccess(success)
            }

        }
    }

    actual fun cancel() {
        isScheduled = false
        BGTaskScheduler.sharedScheduler.cancelTaskRequestWithIdentifier(taskId)
    }

}