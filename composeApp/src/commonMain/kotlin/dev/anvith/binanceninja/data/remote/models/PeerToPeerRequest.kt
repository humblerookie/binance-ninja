package dev.anvith.binanceninja.data.remote.models

import dev.anvith.binanceninja.data.remote.ApiConstants.MAX_ROWS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeerToPeerRequest(
  @SerialName("asset") val asset: String,
  @SerialName("countries") val countries: List<String> = emptyList(),
  @SerialName("fiat") val fiat: String,
  @SerialName("page") val page: Int = 1,
  @SerialName("payTypes") val payTypes: List<String> = emptyList(),
  @SerialName("proMerchantAds") val proMerchantAds: Boolean,
  @SerialName("publisherType") val publisherType: Publisher?,
  @SerialName("rows") val rows: Int = MAX_ROWS,
  @SerialName("tradeType") val tradeType: TradeType,
  @SerialName("transAmount") val amount: Double? = null,
)

@Serializable
enum class Publisher {
  @SerialName("merchant") Merchant,
}

@Serializable
enum class TradeType {
  @SerialName("BUY") Buy,
  @SerialName("SELL") Sell
}
