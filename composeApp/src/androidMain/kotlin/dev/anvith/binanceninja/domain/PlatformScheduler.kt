package dev.anvith.binanceninja.domain

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dev.anvith.binanceninja.core.ui.data.Constants.INTERVAL_DAYS
import dev.anvith.binanceninja.core.ui.data.Constants.INTERVAL_MINUTES
import dev.anvith.binanceninja.data.cache.ResetOrdersTask
import dev.anvith.binanceninja.data.remote.RefreshOrdersTask
import dev.anvith.binanceninja.di.AppScope
import java.util.concurrent.TimeUnit
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class PlatformScheduler(private val context: Context) {
    private lateinit var filterOrdersRequest: PeriodicWorkRequest
    private lateinit var resetOrdersRequest: PeriodicWorkRequest
    private val constraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    private val refreshOrdersKey = "RefreshOrdersTask"
    private val resetOrdersKey = "ResetOrdersTask"

    actual fun schedule(executor: RequestExecutor) {
        // Ignore Parameter on android since we rely on inject
        with(WorkManager.getInstance(context)) {
            filterOrdersRequest =
                PeriodicWorkRequest.Builder(
                    RefreshOrdersTask::class.java,
                    INTERVAL_MINUTES,
                    TimeUnit.MINUTES,
                )
                    .setConstraints(constraints)
                    .build()
            enqueueUniquePeriodicWork(refreshOrdersKey, CANCEL_AND_REENQUEUE, filterOrdersRequest)
            resetOrdersRequest =
                PeriodicWorkRequest.Builder(
                    ResetOrdersTask::class.java,
                    INTERVAL_DAYS,
                    TimeUnit.DAYS,
                )
                    .build()
            enqueueUniquePeriodicWork(resetOrdersKey, CANCEL_AND_REENQUEUE, resetOrdersRequest)
        }
    }

    actual fun cancel() {
        if (::filterOrdersRequest.isInitialized) {
            with(WorkManager.getInstance(context)) {
                cancelWorkById(filterOrdersRequest.id)
                cancelWorkById(resetOrdersRequest.id)
            }
        }
    }
}
