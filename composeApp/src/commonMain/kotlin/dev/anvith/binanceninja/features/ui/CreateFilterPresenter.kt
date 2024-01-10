package dev.anvith.binanceninja.features.ui

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.BasePresenter
import dev.anvith.binanceninja.core.ui.presentation.SideEffect.MiscEffect
import dev.anvith.binanceninja.data.cache.CurrencyRepository
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.domain.mappers.ErrorHandler
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Effect.FilterCreationSuccess
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Effect.NotificationPermissionDenied
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.ActionTypeChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.AmountChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.CreateFilter
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromVerifiedMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.PriceChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.Retry
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.SelectCurrency
import dev.anvith.binanceninja.features.ui.CreateFilterContract.State
import dev.anvith.binanceninja.features.ui.core.PermissionHandler
import dev.anvith.binanceninja.features.ui.core.PermissionType.NOTIFICATION
import dev.anvith.binanceninja.features.ui.validators.CreateFilterValidator
import me.tatarka.inject.annotations.Inject

@Inject
class CreateFilterPresenter(
    private val filterRepository: FilterRepository,
    private val currencyRepository: CurrencyRepository,
    private val validator: CreateFilterValidator,
    private val errorHandler: ErrorHandler,
    private val permissionHandler: PermissionHandler,
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

            is PriceChanged -> updateState { state ->
                state.copy(price = event.value)
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

            is FromVerifiedMerchant -> updateState { state ->
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
            permissionHandler.hasPermission(NOTIFICATION) { hasPermission ->
                if (hasPermission) {
                    saveFilter()
                } else {
                    permissionHandler.requestPermission(NOTIFICATION, onGranted = {
                        saveFilter()
                    }, onDenied = {
                        saveFilter()
                        sideEffect(MiscEffect(NotificationPermissionDenied))
                    })
                }

            }


        }
        updateState { state ->
            state.copy(validationErrors = errors)
        }
    }

    private fun saveFilter() {
        val model = FilterModel(
            isBuy = currentState.isBuy,
            price = currentState.price.text.trim().toDouble(),
            fromMerchant = currentState.fromMerchant,
            isProMerchant = currentState.isRestricted,
            amount = currentState.amount.text.toDouble(),
            targetCurrency = currentState.selectedCurrency!!.code
        )

        launch {
            filterRepository.insertFilter(model)
            sideEffect(MiscEffect(FilterCreationSuccess))
        }
    }


}