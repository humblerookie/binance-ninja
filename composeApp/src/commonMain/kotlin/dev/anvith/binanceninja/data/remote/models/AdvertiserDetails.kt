package dev.anvith.binanceninja.data.remote.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdvertiserDetails(
    @SerialName( "advNo")
    val advNo: String?,
    @SerialName( "asset")
    val asset: String?,
    @SerialName( "buyerBtcPositionLimit")
    val buyerBtcPositionLimit: String?,
    @SerialName( "buyerKycLimit")
    val buyerKycLimit: String?,
    @SerialName( "buyerRegDaysLimit")
    val buyerRegDaysLimit: String?,
    @SerialName( "classify")
    val classify: String?,
    @SerialName( "dynamicMaxSingleTransAmount")
    val dynamicMaxSingleTransAmount: String?,
    @SerialName( "dynamicMaxSingleTransQuantity")
    val dynamicMaxSingleTransQuantity: String?,
    @SerialName( "fiatSymbol")
    val fiatSymbol: String?,
    @SerialName( "fiatUnit")
    val fiatUnit: String?,
    @SerialName( "isTradable")
    val isTradable: Boolean?,
    @SerialName( "maxSingleTransAmount")
    val maxSingleTransAmount: String?,
    @SerialName( "maxSingleTransQuantity")
    val maxSingleTransQuantity: String?,
    @SerialName( "minSingleTransAmount")
    val minSingleTransAmount: String?,
    @SerialName( "minSingleTransQuantity")
    val minSingleTransQuantity: String?,
    @SerialName( "payTimeLimit")
    val payTimeLimit: Int?,
    @SerialName( "price")
    val price: String?,
    @SerialName( "surplusAmount")
    val surplusAmount: String?,
    @SerialName( "tradableQuantity")
    val tradeQty: String?,
    @SerialName( "tradeMethods")
    val tradeMethods: List<TradeMethod?>?,
    @SerialName( "tradeType")
    val tradeType: TradeType
)