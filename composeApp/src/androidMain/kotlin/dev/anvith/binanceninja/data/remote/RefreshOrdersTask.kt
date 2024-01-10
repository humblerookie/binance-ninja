package dev.anvith.binanceninja.data.remote

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.anvith.binanceninja.BinanceApplication
import dev.anvith.binanceninja.core.ui.data.Constants

class RefreshOrdersTask(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    private val periodicScheduler
        get() = (applicationContext as BinanceApplication).appComponent.requestExecutor

    override suspend fun doWork(): Result {
        if (runAttemptCount > Constants.RETRIES) {
            return Result.failure()
        }
        return if (periodicScheduler.executeRequests()) {
            Result.success()
        } else {
            Result.retry()
        }

    }
}