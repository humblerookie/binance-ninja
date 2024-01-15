package dev.anvith.binanceninja.data.cache

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.Constants
import dev.anvith.binanceninja.di.AppComponentProvider

class ResetOrdersTask(
  appContext: Context,
  params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

  private val filterRepository
    get() = (applicationContext as AppComponentProvider).appComponent.filterRepository

  override suspend fun doWork(): Result {
    if (runAttemptCount > Constants.RETRIES) {
      return Result.failure()
    }
    return try {
      filterRepository.resetOrders()
      Result.success()
    } catch (e: Exception) {
      logE("Failed to reset orders", e)
      Result.retry()
    }
  }
}
