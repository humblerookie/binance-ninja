package dev.anvith.binanceninja.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TradeSide(
  @SerialName("assets") val assets: List<PeerToPeerAsset>,
  @SerialName("side") val side: TradeType,
)
