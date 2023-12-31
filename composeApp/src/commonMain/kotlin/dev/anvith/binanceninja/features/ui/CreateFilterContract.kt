package dev.anvith.binanceninja.features.ui

import dev.anvith.binanceninja.core.ui.data.IMap
import dev.anvith.binanceninja.core.ui.data.emptyIMap

interface CreateFilterContract {
    sealed class Event {
        data object CreateFilter : Event()
        data class MinChanged(val value: String) : Event()
        data class MaxChanged(val value: String) : Event()
        data class AmountChanged(val value: String) : Event()
        data class ActionTypeChanged(val isBuy: Boolean) : Event()
        data class FromMerchant(val value: Boolean) : Event()
        data class IsRestricted(val value: Boolean) : Event()
    }


    data class State(
        val isBuy: Boolean = true,
        val min: String = "",
        val max: String = "",
        val fromMerchant: Boolean = false,
        val isRestricted: Boolean = false,
        val amount: String = "",
        val errors: IMap<ErrorTarget, Boolean> = emptyIMap()
    )

    enum class ErrorTarget {
        MIN_MAX, AMOUNT
    }
}