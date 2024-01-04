package dev.anvith.binanceninja.data.remote.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TradeMethod(
    @SerialName("identifier")
    val identifier: String?,
    @SerialName("tradeMethodName")
    val tradeMethodName: String?
)