package dev.anvith.binanceninja.data.remote.core

import dev.anvith.binanceninja.data.remote.core.ApiResult.Failure
import dev.anvith.binanceninja.data.remote.core.ApiResult.Success
import dev.anvith.binanceninja.data.remote.extensions.asResult
import io.ktor.client.statement.HttpResponse

/** A sealed wrapper class to wrap the results of all API calls */
sealed class ApiResult<out T> {
  class Success<out T>(val data: T) : ApiResult<T>()

  class Failure(val throwable: Throwable, val message: String? = null) : ApiResult<Nothing>()

  companion object {
    suspend inline fun <reified T> getResult(apiCall: () -> HttpResponse): ApiResult<T> {
      return try {
        val result = apiCall()
        result.asResult<T>()
      } catch (e: Throwable) {
        Failure(e)
      }
    }
  }
}

inline fun <T> ApiResult<T>.whenSuccess(block: (result: T) -> Unit): ApiResult<T> {
  if (this is Success && data != null) {
    block(this.data)
  }
  Result
  return this
}

inline fun <T> ApiResult<T>.whenError(block: (error: Throwable) -> Unit): ApiResult<T> {
  if (this is Failure) {
    block(this.throwable)
  }
  return this
}

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> =
  try {
    Success(transform((this as Success).data))
  } catch (e: Throwable) {
    Failure(e)
  }
