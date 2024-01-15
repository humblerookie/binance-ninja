package dev.anvith.binanceninja.data.remote

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.anvith.binanceninja.BinanceApplication

class RefreshOrdersTask(
  appContext: Context,
  params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

  private val periodicScheduler
    get() = (applicationContext as BinanceApplication).appComponent.requestExecutor

  override suspend fun doWork(): Result {
    return if (periodicScheduler.executeRequests()) {
      Result.success()
    } else {
      Result.failure()
    }
  }
}
