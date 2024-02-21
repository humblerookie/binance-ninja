package dev.anvith.binanceninja.data.remote.models

import dev.anvith.binanceninja.data.remote.serialization.AreaTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TradeArea(
  @SerialName("area") @Serializable(with = AreaTypeSerializer::class) val area: AreaType,
  @SerialName("tradeSides") val tradeSides: List<TradeSide>
)
