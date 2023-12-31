package dev.anvith.binanceninja.domain.models

data class Order(
    val id: String,
    val advertiser: String,
    val advertiserId: String,
    val price: Float,
    val currency: String,
    val asset: String,
    val isBuy: Boolean,
    val paymentModes: List<PaymentMode>,
    val minAmount: Float,
    val maxAmount: Float,
    val qty: Float,
)

data class PaymentMode(
    val id: String,
    val name: String,
)
