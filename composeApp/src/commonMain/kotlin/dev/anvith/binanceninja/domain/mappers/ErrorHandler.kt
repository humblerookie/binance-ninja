package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.core.res.getLocaleStrings
import dev.anvith.binanceninja.di.AppScope
import io.ktor.client.network.sockets.SocketTimeoutException
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class ErrorHandler {

  fun getMessage(throwable: Throwable): String {
    return getLocaleStrings().run {
      when (throwable) {
        is SocketTimeoutException -> errorNetwork
        is Exception -> errorCommon
        else -> errorSync
      }
    }
  }
}
