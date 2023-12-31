package dev.anvith.binanceninja.domain.models

import dev.anvith.binanceninja.core.ui.data.Constants

data class FilterModel(
    val id: Long = 0,
    val isBuy: Boolean,
    val min: Double?,
    val max: Double?,
    val fromMerchant: Boolean,
    val isRestricted: Boolean,
    val amount: Double,
    val sourceCurrency: String = Constants.USDT,
    val targetCurrency: String = Constants.INR,
)
