package dev.anvith.binanceninja.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigResponse(
  @SerialName("areas") val areas: List<TradeArea>,
  @SerialName("fiat") val fiat: String,
)
