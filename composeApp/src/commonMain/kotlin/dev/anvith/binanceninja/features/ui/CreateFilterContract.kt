package dev.anvith.binanceninja.features.ui

import androidx.compose.ui.text.input.TextFieldValue
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.IMap
import dev.anvith.binanceninja.core.ui.data.emptyIList
import dev.anvith.binanceninja.core.ui.data.emptyIMap
import dev.anvith.binanceninja.domain.models.CurrencyModel
import dev.anvith.binanceninja.domain.models.ErrorStrings

interface CreateFilterContract {
    sealed class Event {
        data object CreateFilter : Event()
        data object Retry : Event()
        data class MinChanged(val value: TextFieldValue) : Event()
        data class MaxChanged(val value: TextFieldValue) : Event()
        data class AmountChanged(val value: TextFieldValue) : Event()
        data class ActionTypeChanged(val isBuy: Boolean) : Event()
        data class FromMerchant(val value: Boolean) : Event()
        data class IsRestricted(val value: Boolean) : Event()
        data class SelectCurrency(val value: CurrencyModel) : Event()
    }


    data class State(
        val isBuy: Boolean = true,
        val min: TextFieldValue = TextFieldValue(),
        val max: TextFieldValue = TextFieldValue(),
        val fromMerchant: Boolean = false,
        val isRestricted: Boolean = false,
        val amount: TextFieldValue = TextFieldValue(),
        val validationErrors: IMap<ErrorTarget, Boolean> = emptyIMap(),
        val currencies: IList<CurrencyModel> = emptyIList(),
        val selectedCurrency: CurrencyModel? = null,
        val errorMessage: ErrorStrings? = null,
        val isLoading: Boolean = false,
    )

    sealed class Effect {
        data object FilterCreationSuccess : Effect()
    }

    enum class ErrorTarget {
        MIN, MAX, AMOUNT, CURRENCY
    }
}