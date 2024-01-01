package dev.anvith.binanceninja.features.ui

import androidx.compose.ui.text.input.TextFieldValue
import dev.anvith.binanceninja.core.ui.data.IMap
import dev.anvith.binanceninja.core.ui.data.emptyIMap

interface CreateFilterContract {
    sealed class Event {
        data object CreateFilter : Event()
        data class MinChanged(val value: TextFieldValue) : Event()
        data class MaxChanged(val value: TextFieldValue) : Event()
        data class AmountChanged(val value: TextFieldValue) : Event()
        data class ActionTypeChanged(val isBuy: Boolean) : Event()
        data class FromMerchant(val value: Boolean) : Event()
        data class IsRestricted(val value: Boolean) : Event()
    }


    data class State(
        val isBuy: Boolean = true,
        val min: TextFieldValue = TextFieldValue(),
        val max: TextFieldValue = TextFieldValue(),
        val fromMerchant: Boolean = false,
        val isRestricted: Boolean = false,
        val amount: TextFieldValue = TextFieldValue(),
        val errors: IMap<ErrorTarget, Boolean> = emptyIMap()
    )

    sealed class Effect {
        data object FilterCreationSuccess : Effect()
    }

    enum class ErrorTarget {
        MIN,MAX, AMOUNT
    }
}