package dev.anvith.binanceninja.domain.models

typealias Orders = List<Order>
data class Order(
    val id: String,
    val advertiser: String,
    val advertiserId: String,
    val price: Double,
    val currency: String,
    val asset: String,
    val isBuy: Boolean,
    val paymentModes: List<PaymentMode>,
    val minAmount: Double,
    val maxAmount: Double,
    val qty: Double,
)

data class PaymentMode(
    val id: String,
    val name: String,
)
