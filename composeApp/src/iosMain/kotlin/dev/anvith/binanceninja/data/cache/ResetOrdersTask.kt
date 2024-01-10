package dev.anvith.binanceninja.data.cache

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.extensions.toException
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.Constants
import dev.anvith.binanceninja.data.BackgroundTask
import dev.anvith.binanceninja.domain.RequestExecutor
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
class ResetOrdersTask(
    private val dispatcherProvider: DispatcherProvider,
    private val filterRepository: FilterRepository,
) : BackgroundTask {
    override val taskId = "dev.anvith.binanceninja.FilterOrders.reset"
    override var isScheduled = false
    private var executor: RequestExecutor? = null
    private fun getScope() =
        CoroutineScope(SupervisorJob() + dispatcherProvider.io() + CoroutineExceptionHandler { _, exception ->
            logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
        })

    override fun schedule(executor: RequestExecutor) {
        this.executor = executor
        scheduleRequest()
    }

    override fun cancel() {
        isScheduled = false
        BGTaskScheduler.sharedScheduler.cancelTaskRequestWithIdentifier(taskId)
    }

    override fun registerHandler() =
        BGTaskScheduler.sharedScheduler().registerForTaskWithIdentifier(
            taskId,
            null,
        ) { task ->
            val scope = getScope()
            task?.expirationHandler = { scope.cancel() }
            scheduleRequest()
            scope.launch {
                try {
                    filterRepository.resetOrders()
                    task?.setTaskCompletedWithSuccess(true)
                } catch (e: Exception) {
                    logE("Error resetting orders", e)
                    task?.setTaskCompletedWithSuccess(false)
                }
            }

        }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun scheduleRequest() {
        if (!isScheduled) {
            try {
                val request = BGAppRefreshTaskRequest(taskId)
                request.earliestBeginDate = getNextScheduledTime()
                memScoped {
                    val error = interpretCPointer<ObjCObjectVar<NSError?>>(alloc(1, 1).rawPtr)!!
                    isScheduled = BGTaskScheduler.sharedScheduler.submitTaskRequest(request, error)
                    if (!isScheduled) {
                        throw error[0].value!!.toException()
                    }
                }

            } catch (e: Exception) {
                logE("Error submitting task request $e")
            }
        }
    }

    private fun getNextScheduledTime(): NSDate {
        val currentDate = NSDate()
        val calendar = NSCalendar.currentCalendar
        val components = NSDateComponents().apply {
                day = Constants.INTERVAL_DAYS
        }
        return calendar.dateByAddingComponents(components, currentDate, 0u)!!
    }

}