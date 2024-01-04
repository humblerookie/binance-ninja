package dev.anvith.binanceninja.data.remote.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseApiModel<T>(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String?,
    @SerialName("messageDetail")
    val messageDetail: String?,
    @SerialName("data")
    val data: T
)