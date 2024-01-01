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
    override val labelGreaterThan = "Greater Than (>=)"
    override val labelLessThan = "Less Than (<=)"
    override val selectAmount = "Select the amount:"
    override val selectPrice = "Select the price:"
    override val miscRequirements = "Miscellaneous: "
    override val commonError = "Something went wrong"
    override val miscOptions = listOf("Is Merchant", "Is Restricted")
    override val labelMerchant = "Merchant"
    override val labelRestricted = "Restricted"
    override val priceDynamicsLabel =
        { currency: String, start: Double?, to: Double?, labelColor: Color ->
            buildAnnotatedString {
                val symbol = getCurrencySymbol(currency)
                withStyle(SpanStyle(color = labelColor)) {
                    append("at price ")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Medium, fontSize = Dimens.textXLarge)) {
                    append(
                        "$symbol${
                            start?.let { formatPrecision(it) }.orEmpty()
                        }-$symbol${to?.let { formatPrecision(it) }.orEmpty()}"
                    )
                }
            }
        }
    override val actionDynamicsLabel =
        { currency: String, amount: Double, isBuy: Boolean, labelColor: Color ->
            buildAnnotatedString {
                withStyle(SpanStyle(color = labelColor)) {
                    append(if (isBuy) actionBuy else actionSell)
                }
                append(" ${getCurrencySymbol(currency)} ${formatPrecision(amount)}")
            }
        }
    override val labelRemove = "Remove"

    override val errorMinMaxEmpty = "Provide either min or max"

    override val errorAmountEmpty = "Provide a quantity"

    override val errorInvalidInput= "Invalid input"

    override val errorNoFilters = "You currently have no filters added."

    override val filterCreationMessage = "Filter created successfully"
}