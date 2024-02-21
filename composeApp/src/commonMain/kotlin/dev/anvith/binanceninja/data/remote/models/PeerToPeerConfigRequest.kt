package dev.anvith.binanceninja.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeerToPeerConfigRequest(
  @SerialName("fiat") val fiat: String,
)