package dev.anvith.binanceninja.data.remote

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import me.tatarka.inject.annotations.Provides

actual interface PlatformHttpComponent {
  @Provides
  fun httpClientEngine(): HttpClientEngine {
    return Darwin.create()
  }
}
