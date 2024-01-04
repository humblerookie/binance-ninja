package dev.anvith.binanceninja.features.ui

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.BasePresenter
import dev.anvith.binanceninja.core.ui.presentation.SideEffect.MiscEffect
import dev.anvith.binanceninja.data.cache.CurrencyRepository
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.domain.mappers.ErrorHandler
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Effect
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.ActionTypeChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.AmountChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.CreateFilter
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.IsRestricted
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MaxChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MinChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.Retry
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.SelectCurrency
import dev.anvith.binanceninja.features.ui.CreateFilterContract.State
import dev.anvith.binanceninja.features.ui.validators.CreateFilterValidator
import me.tatarka.inject.annotations.Inject

@Inject
class CreateFilterPresenter(
    private val filterRepository: FilterRepository,
    private val currencyRepository: CurrencyRepository,
    private val validator: CreateFilterValidator,
    private val errorHandler: ErrorHandler,
    dispatcherProvider: DispatcherProvider,
) : BasePresenter<State, Event>(dispatcherProvider) {


    override fun initState(): State = State()

    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        launch {
            updateState { state ->
                state.copy(isLoading = true, errorMessage = null)
            }
            currencyRepository.getAllFiatCurrencies(onSyncFailure = ::onFetchError).collect {
                updateState { state ->
                    state.copy(
                        currencies = it.lock(),
                        selectedCurrency = state.selectedCurrency
                            ?: it.firstOrNull { it.code == currencyRepository.getUserCurrency() },
                        isLoading = it.isEmpty()
                    )
                }
            }
        }
    }

    private fun onFetchError(throwable: Throwable) {
        updateState {
            it.copy(errorMessage = errorHandler.getMessage(throwable), isLoading = false)
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is CreateFilter -> createFilter()

            is MinChanged -> updateState { state ->
                state.copy(min = event.value)
            }

            is MaxChanged -> updateState { state ->
                state.copy(max = event.value)
            }

            is AmountChanged -> updateState { state ->
                state.copy(amount = event.value)
            }

            is ActionTypeChanged -> updateState { state ->
                state.copy(isBuy = event.isBuy)
            }

            is FromMerchant -> updateState { state ->
                state.copy(fromMerchant = event.value)
            }

            is IsRestricted -> updateState { state ->
                state.copy(isRestricted = event.value)
            }

            is SelectCurrency -> {
                updateState { state ->
                    state.copy(selectedCurrency = event.value)
                }
                currencyRepository.saveUserCurrency(event.value)
            }

            Retry -> fetchCurrencies()
        }
    }

    private fun createFilter() {
        val errors = validator.validate(currentState)
        if (errors.isEmpty()) {
            val model = FilterModel(
                isBuy = currentState.isBuy,
                min = currentState.min.text.trim().toDoubleOrNull(),
                max = currentState.max.text.trim().toDoubleOrNull(),
                fromMerchant = currentState.fromMerchant,
                isRestricted = currentState.isRestricted,
                amount = currentState.amount.text.toDouble(),
                targetCurrency = currentState.selectedCurrency!!.code
            )
            launch {
                filterRepository.insertFilter(model)
                sideEffect(MiscEffect(Effect.FilterCreationSuccess))
            }

        }
        updateState { state ->
            state.copy(validationErrors = errors)
        }
    }


}