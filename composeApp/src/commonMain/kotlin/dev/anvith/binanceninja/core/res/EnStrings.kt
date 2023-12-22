package dev.anvith.binanceninja.core.res

import cafe.adriel.lyricist.LyricistStrings

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
    override val miscRequirements = "Miscellaneous: "
    override val commonError = "Something went wrong"
    override val miscOptions = listOf("Is Merchant", "Is Restricted")
}