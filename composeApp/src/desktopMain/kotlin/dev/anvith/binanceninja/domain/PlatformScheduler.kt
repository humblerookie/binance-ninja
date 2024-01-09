package dev.anvith.binanceninja.domain

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.Constants
import dev.anvith.binanceninja.di.AppScope
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject


@Inject
@AppScope
actual class PlatformScheduler(
    dispatcherProvider: DispatcherProvider,
) {


    private val scope =
        CoroutineScope(SupervisorJob() + dispatcherProvider.io() + CoroutineExceptionHandler { _, exception ->
            logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
        })
    private var scheduledExecutorService: ScheduledExecutorService? = null
    private var future: ScheduledFuture<*>? = null
    actual fun schedule(executor: RequestExecutor) {
        scheduledExecutorService = Executors.newScheduledThreadPool(Constants.PARALLELISM)
        val task = Runnable {
            scope.launch {
                var count = 0
                var success = false
                while (count <= Constants.RETRIES && !success) {
                    success = executor.executeRequests()
                    count++
                }
            }
        }

        future = scheduledExecutorService!!.scheduleAtFixedRate(
            task,
            0,
            Constants.INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )

    }

    actual fun cancel() {
        future?.cancel(true)
        scheduledExecutorService?.shutdown()
    }

}