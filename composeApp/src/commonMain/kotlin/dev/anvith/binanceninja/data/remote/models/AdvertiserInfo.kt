package dev.anvith.binanceninja.data.remote.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdvertiserInfo(
    @SerialName( "nickName")
    val nickName: String?,
    @SerialName( "userNo")
    val userNo: String?
)