package  dev.anvith.binanceninja.data.remote.models

import dev.anvith.binanceninja.data.remote.ApiConstants.MAX_ROWS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeerToPeerRequest(
    val asset: String,
    val countries: List<String> = emptyList(),
    val fiat: String,
    val page: Int = 1,
    val payTypes: List<String> = emptyList(),
    val proMerchantAds: Boolean,
    val publisherType: Publisher,
    val rows: Int = MAX_ROWS,
    val tradeType: TradeType
)

@Serializable
enum class Publisher {
    @SerialName("merchant")
    Merchant,

    @SerialName("any")
    Any
}

@Serializable
enum class TradeType {
    @SerialName("BUY")
    Buy,

    @SerialName("SELL")
    Sell
}