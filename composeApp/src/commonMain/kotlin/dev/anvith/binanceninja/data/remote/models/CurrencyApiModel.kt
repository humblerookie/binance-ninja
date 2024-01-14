package dev.anvith.binanceninja.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyApiModel(
  @SerialName("countryCode") val countryCode: String,
  @SerialName("currencyCode") val currencyCode: String,
  @SerialName("currencySymbol") val currencySymbol: String,
  @SerialName("iconUrl") val iconUrl: String?
)
