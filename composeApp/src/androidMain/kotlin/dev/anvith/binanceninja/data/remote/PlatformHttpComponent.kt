package dev.anvith.binanceninja.data.remote

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import me.tatarka.inject.annotations.Provides

actual interface PlatformHttpComponent {

  @Provides
  fun httpClientEngine(okHttpConfig: OkHttpConfig): HttpClientEngine {
    return OkHttpEngine(okHttpConfig)
  }

  @Provides
  fun httpConfig(): OkHttpConfig {
    // Generic Configuration is done in the common module
    // See
    // [ApiComponent](../../commonMain/kotlin/dev/anvith/binanceninja/data/remote/ApiComponent.kt)
    return OkHttpConfig()
  }
}
