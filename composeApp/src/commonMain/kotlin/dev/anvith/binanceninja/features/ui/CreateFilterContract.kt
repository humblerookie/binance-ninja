package dev.anvith.binanceninja.features.ui

import androidx.compose.ui.text.input.TextFieldValue
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.IMap
import dev.anvith.binanceninja.core.ui.data.emptyIList
import dev.anvith.binanceninja.core.ui.data.emptyIMap
import dev.anvith.binanceninja.domain.models.CurrencyModel

interface CreateFilterContract {
  sealed class Event {
    data object CreateFilter : Event()

    data object Retry : Event()

    data class PriceChanged(val value: TextFieldValue) : Event()

    data class AmountChanged(val value: TextFieldValue) : Event()

    data class ActionTypeChanged(val isBuy: Boolean) : Event()

    data class FromMerchant(val value: Boolean) : Event()

    data class FromVerifiedMerchant(val value: Boolean) : Event()

    data class SelectCurrency(val value: CurrencyModel) : Event()
  }

  data class State(
    val isBuy: Boolean = true,
    val price: TextFieldValue = TextFieldValue(),
    val fromMerchant: Boolean = false,
    val isRestricted: Boolean = false,
    val amount: TextFieldValue = TextFieldValue(),
    val validationErrors: IMap<ErrorTarget, Boolean> = emptyIMap(),
    val currencies: IList<CurrencyModel> = emptyIList(),
    val selectedFiatCurrency: CurrencyModel? = null,
    val selectedCryptoCurrency: CurrencyModel? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
  )

  sealed class Effect {
    data object FilterCreationSuccess : Effect()

    data object NotificationPermissionDenied : Effect()
  }

  enum class ErrorTarget {
    PRICE,
    AMOUNT,
    CURRENCY
  }
}
