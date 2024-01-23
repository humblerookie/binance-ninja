package dev.anvith.binanceninja.core

import dev.anvith.binanceninja.BuildKonfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.sentry.kotlin.multiplatform.Sentry
import me.tatarka.inject.annotations.Inject

@Inject
class LoggingInitializer : Initializer {

  override fun initialize() {
    Sentry.init { options ->
      options.dsn = BuildKonfig.SENTRY_DSN
      options.environment = BuildKonfig.SENTRY_ENVIRONMENT
      options.debug = BuildKonfig.IS_DEBUG
    }
    Napier.base(DebugAntilog())
  }
}
