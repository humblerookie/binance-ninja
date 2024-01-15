package dev.anvith.binanceninja.data.remote

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
class RefreshOrdersTask(
  private val dispatcherProvider: DispatcherProvider,
) : BackgroundTask {
  override val taskId = "dev.anvith.binanceninja.FilterOrders.refresh"
  override var isScheduled = false
  private var executor: RequestExecutor? = null

  private fun getScope() =
    CoroutineScope(
      SupervisorJob() +
        dispatcherProvider.io() +
        CoroutineExceptionHandler { _, exception ->
          logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
        }
    )

  override fun schedule(executor: RequestExecutor) {
    this.executor = executor
    scheduleRequest(executeNow = true)
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
        val isSuccess = executor?.executeRequests() ?: false
        task?.setTaskCompletedWithSuccess(isSuccess)
      }
    }

  @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
  private fun scheduleRequest(executeNow: Boolean = false) {
    if (!isScheduled) {
      try {
        val request = BGAppRefreshTaskRequest(taskId)
        request.earliestBeginDate = getNextScheduledTime(executeNow)
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

  private fun getNextScheduledTime(executeNow: Boolean): NSDate {
    val currentDate = NSDate()
    val calendar = NSCalendar.currentCalendar
    val components =
      NSDateComponents().apply {
        if (executeNow) {
          second = 5
        } else {
          minute = Constants.INTERVAL_MINUTES
        }
      }
    return calendar.dateByAddingComponents(components, currentDate, 0u)!!
  }
}
