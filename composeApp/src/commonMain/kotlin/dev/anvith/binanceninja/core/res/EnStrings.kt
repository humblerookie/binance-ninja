package dev.anvith.binanceninja.core.res

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings =
  object : Strings {
    override val changeFiat = "Change Fiat"
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
    override val selectAmount = "Select the amount:"
    override val selectPrice = "Enter the price"
    override val miscRequirements = "Miscellaneous: "
    override val miscOptions = listOf("Is Merchant", "Is Pro Merchant")
    override val labelMerchant = "Merchant"
    override val labelProMerchant = "Pro Merchant"
    override val labelAmount = "Amount"
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

    override val  matchNotificationTitle= "Match Notifications"
    override val  matchNotificationDescription= "All order notifications that match your filter."
  }
