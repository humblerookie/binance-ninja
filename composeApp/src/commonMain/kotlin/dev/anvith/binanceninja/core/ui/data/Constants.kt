package dev.anvith.binanceninja.core.ui.data

object Constants {

  const val FORMAT_PRECISION = 8

  const val INR = "INR"
  const val INR_SYMBOL = "₹"
  const val USDT = "USDT"
  const val RETRIES = 5
  const val PARALLELISM = 2
  const val INTERVAL_MINUTES = 15L
  const val INTERVAL_DAYS = 1L
  const val RATE_LIMIT_DELAY = 150L
  const val APP_SCHEME = "binanceninja://"

  object Assets {

    const val EMPTY = "empty-asset.xml"
    const val BUY = "ic-buy.xml"
    const val SELL = "ic-sell.xml"
    const val SWITCH = "ic-switch.xml"
  }
}
