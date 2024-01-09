package dev.anvith.binanceninja.domain

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dev.anvith.binanceninja.core.ui.data.Constants.INTERVAL_MINUTES
import dev.anvith.binanceninja.data.remote.OrderSearchWork
import dev.anvith.binanceninja.di.AppScope
import java.util.concurrent.TimeUnit
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class PlatformScheduler(private val context: Context) {
    private lateinit var workRequest: PeriodicWorkRequest
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    actual fun schedule(executor:RequestExecutor) {
        // Ignore Parameter on android since we rely on inject
        workRequest = PeriodicWorkRequest.Builder(
            OrderSearchWork::class.java,
            INTERVAL_MINUTES,
            TimeUnit.MINUTES,
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)

    }

    actual fun cancel() {
        if (::workRequest.isInitialized) {
            WorkManager.getInstance(context).cancelWorkById(workRequest.id)
        }
    }


}