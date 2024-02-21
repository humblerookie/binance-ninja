package dev.anvith.binanceninja.data.remote.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeerToPeerAsset(
    @SerialName("asset")
    val asset: String,
    @SerialName("description")
    val name: String?,
    @SerialName("iconUrl")
    val iconUrl: String?,
)