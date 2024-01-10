package dev.anvith.binanceninja.domain.models

import dev.anvith.binanceninja.core.ui.data.Constants

data class FilterModel(
    val id: Long = 0,
    val isBuy: Boolean,
    val price: Double,
    val fromMerchant: Boolean,
    val isProMerchant: Boolean,
    val amount: Double,
    val sourceCurrency: String = Constants.USDT,
    val targetCurrency: String = Constants.INR,
)
