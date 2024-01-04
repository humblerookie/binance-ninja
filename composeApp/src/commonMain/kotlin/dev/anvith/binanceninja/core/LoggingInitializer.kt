package dev.anvith.binanceninja.core

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import me.tatarka.inject.annotations.Inject

@Inject
class LoggingInitializer : Initializer {

    override fun initialize() {
        Napier.base(DebugAntilog())
    }
}