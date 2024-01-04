package dev.anvith.binanceninja.data.remote.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeerToPeerOrder(
    @SerialName( "adv")
    val adv: AdvertiserDetails?,
    @SerialName( "advertiser")
    val advertiser: AdvertiserInfo?
)

typealias  PeerToPeerOrders = List<PeerToPeerOrder>