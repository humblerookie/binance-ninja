package dev.anvith.binanceninja

import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.emptyIList
import dev.anvith.binanceninja.domain.models.CurrencyModel

interface AppContract {
    sealed class Event {
        data object Retry : Event()
        data class SelectCurrency(val currency: CurrencyModel) : Event()
    }

    data class State(
        val userCurrency: CurrencyModel? = null,
        val currencies: IList<CurrencyModel> = emptyIList(),
        val errorMessage: String? = null,
        val isLoading:Boolean = false,
    )

    sealed class Effect {
        data class DisplayError(val message: String) : Effect()
    }
}
