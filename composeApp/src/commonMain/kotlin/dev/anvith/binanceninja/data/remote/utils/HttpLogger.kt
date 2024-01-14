package dev.anvith.binanceninja.data.remote.utils

import dev.anvith.binanceninja.core.logD
import io.ktor.client.plugins.logging.Logger

object HttpLogger : Logger {
  override fun log(message: String) = logD(message)
}
