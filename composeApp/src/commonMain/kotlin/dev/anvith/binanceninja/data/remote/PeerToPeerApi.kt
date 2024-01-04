package dev.anvith.binanceninja.data.remote

import dev.anvith.binanceninja.data.remote.core.ResponseApiModel
import dev.anvith.binanceninja.data.remote.extensions.asResult
import dev.anvith.binanceninja.data.remote.extensions.post
import dev.anvith.binanceninja.data.remote.models.ApiResult
import dev.anvith.binanceninja.data.remote.models.CurrencyApiModel
import dev.anvith.binanceninja.data.remote.models.PeerToPeerOrders
import dev.anvith.binanceninja.data.remote.models.PeerToPeerRequest
import io.ktor.client.HttpClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


interface PeerToPeerApi {
    suspend fun getFiatCurrencies(): ApiResult<ResponseApiModel<List<CurrencyApiModel>>>

    suspend fun getOrders(request: PeerToPeerRequest): ApiResult<ResponseApiModel<PeerToPeerOrders>>
}

class PeerToPeerApiImpl(
    private val client: HttpClient,
    private val json: Json,
) : PeerToPeerApi {
    override suspend fun getFiatCurrencies(): ApiResult<ResponseApiModel<List<CurrencyApiModel>>> {
        return client.post(
            endpoint = Endpoints.CURRENCY,
            jsonBody = "",
        ).asResult()
    }

    override suspend fun getOrders(request: PeerToPeerRequest): ApiResult<ResponseApiModel<PeerToPeerOrders>> {
        return client.post(
            endpoint = Endpoints.ORDERS,
            jsonBody = json.encodeToString(request),
        ).asResult()
    }
}