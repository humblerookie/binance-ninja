package dev.anvith.binanceninja.data.remote

import dev.anvith.binanceninja.data.remote.ApiConstants.TIMEOUT
import dev.anvith.binanceninja.data.remote.utils.HttpLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

internal interface ApiComponent : PlatformHttpComponent {
    @Provides
    fun httpClient(
        engine: HttpClientEngine,
        json: Json,
    ): HttpClient {
        return HttpClient(engine) {
            install(HttpTimeout) {
                requestTimeoutMillis = TIMEOUT
                socketTimeoutMillis = TIMEOUT
                connectTimeoutMillis = TIMEOUT
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = HttpLogger
                level = LogLevel.ALL
            }
        }
    }

    @Provides
    fun jsonConfig(): Json {
        return Json {
            isLenient = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }


    @Provides
    fun peerToPeerApi(client: HttpClient, json: Json): PeerToPeerApi =
        PeerToPeerApiImpl(client, json)
}