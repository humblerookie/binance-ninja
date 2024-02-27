package dev.anvith.binanceninja.data.remote

import dev.anvith.binanceninja.data.remote.core.ResponseApiModel
import dev.anvith.binanceninja.data.remote.extensions.post
import dev.anvith.binanceninja.data.remote.core.ApiResult
import dev.anvith.binanceninja.data.remote.models.ConfigResponse
import dev.anvith.binanceninja.data.remote.models.CurrencyApiModel
import dev.anvith.binanceninja.data.remote.models.PeerToPeerConfigRequest
import dev.anvith.binanceninja.data.remote.models.PeerToPeerOrders
import dev.anvith.binanceninja.data.remote.models.PeerToPeerRequest
import io.ktor.client.HttpClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface PeerToPeerApi {
    suspend fun getFiatCurrencies(): ApiResult<ResponseApiModel<List<CurrencyApiModel>>>

    suspend fun getOrders(request: PeerToPeerRequest): ApiResult<ResponseApiModel<PeerToPeerOrders>>

    suspend fun getCryptoCurrencies(
        request: PeerToPeerConfigRequest
    ): ApiResult<ResponseApiModel<ConfigResponse>>
}

class PeerToPeerApiImpl(
    private val client: HttpClient,
    private val json: Json,
) : PeerToPeerApi {
    override suspend fun getFiatCurrencies(): ApiResult<ResponseApiModel<List<CurrencyApiModel>>> {
        return ApiResult.getResult {
            client
                .post(
                    endpoint = Endpoints.CURRENCY,
                    jsonBody = "",
                )
        }
    }

    override suspend fun getOrders(
        request: PeerToPeerRequest
    ): ApiResult<ResponseApiModel<PeerToPeerOrders>> {
        return ApiResult.getResult {
            client
                .post(
                    endpoint = Endpoints.ORDERS,
                    jsonBody = json.encodeToString(request),
                )
        }
    }

    override suspend fun getCryptoCurrencies(
        request: PeerToPeerConfigRequest
    ): ApiResult<ResponseApiModel<ConfigResponse>> {
        return ApiResult.getResult {
            client
                .post(endpoint = Endpoints.CONFIG, jsonBody = json.encodeToString(request))
        }
    }
}
