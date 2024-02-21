package dev.anvith.binanceninja.data.remote

object ApiConstants {

  const val BASE_URl = "https://p2p.binance.com/bapi/c2c/"

  const val TIMEOUT = 60000L

  const val MAX_ROWS = 20
}

object Endpoints {
  const val CURRENCY = "v1/friendly/c2c/trade-rule/fiat-list"
  const val ORDERS = "v2/friendly/c2c/adv/search"
  const val CONFIG = "v2/friendly/c2c/portal/config"
}
