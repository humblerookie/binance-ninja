package dev.anvith.binanceninja.core.res

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cafe.adriel.lyricist.LyricistStrings
import dev.anvith.binanceninja.core.ui.data.formatPrecision
import dev.anvith.binanceninja.core.ui.data.getCurrencySymbol
import dev.anvith.binanceninja.core.ui.theme.Dimens

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = object : Strings {
    override val appName = "Binance Ninja"
    override val actionBuy = "Buy"
    override val actionSell = "Sell"
    override val labelPrice = "Price"
    override val tabCreateFilter = "Create Filter"
    override val tabViewFilters = "View Filter"
    override val buySellPrompt = "Select the kind of action: "
    override val actionCreateFilter = "Create Filter"
    override val labelGreaterThan = "When price is at least:"
    override val labelLessThan = "When price is less than:"
    override val selectAmount =
        { currency: String? -> "Select the amount ${currency?.let { "(in $it)" } ?: ""}:" }
    override val selectPrice = "Enter the price"
    override val miscRequirements = "Miscellaneous: "
    override val miscOptions = listOf("Is Merchant", "Is Pro Merchant")
    override val labelMerchant = "Merchant"
    override val labelRestricted = "Restricted"
    override val priceDynamicsLabel =
        { currency: String, price: Double,  labelColor: Color ->
            buildAnnotatedString {
                val symbol = getCurrencySymbol(currency)
                withStyle(SpanStyle(color = labelColor)) {
                    append("at price ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Medium, fontSize = Dimens.textXLarge)) {
                    append("$symbol${formatPrecision(price)}")
                }
            }
        }
    override val actionDynamicsLabel =
        { currency: String, targetCurrency:String, isBuy: Boolean, amount: Double,  labelColor: Color ->
            buildAnnotatedString {
                withStyle(SpanStyle(color = labelColor)) {
                    append(if (isBuy) actionBuy else actionSell)
                }
                append(" $currency")
                withStyle(SpanStyle(color = labelColor)) {
                    append(" worth ")
                }
                append("${getCurrencySymbol(targetCurrency)} ${formatPrecision(amount)}")
            }
        }
    override val labelRemove = "Remove"

    override val errorCommon = "Something went wrong. Please try again."
    override val errorNetwork =
        "Network request failed. Please check your connection and try again."
    override val selectCurrency = "Select a currency"
    override val actionRetry = "Retry"
    override val errorSync = "Failed to sync data. Please try again."
    override val errorMinMaxEmpty = "Provide either min or max"
    override val errorAmountEmpty = "Provide a quantity"
    override val errorInvalidInput = "Invalid input"
    override val errorNoFilters = "You currently have no filters added."
    override val filterCreationMessage = "Filter created successfully"
    override val permissionDeniedNotifications =
        "Notification permission was denied, You may not receive order match notifications. Please enable them from app settings."

}