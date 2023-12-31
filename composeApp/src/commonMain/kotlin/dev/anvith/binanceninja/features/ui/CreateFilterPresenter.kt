package dev.anvith.binanceninja.features.ui

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.ui.presentation.BasePresenter
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.ActionTypeChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.AmountChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.CreateFilter
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.IsRestricted
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MaxChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MinChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.State
import dev.anvith.binanceninja.features.ui.validators.CreateFilterValidator
import me.tatarka.inject.annotations.Inject

@Inject
class CreateFilterPresenter(
    private val repository: FilterRepository,
    private val validator: CreateFilterValidator,
    dispatcherProvider: DispatcherProvider,
) : BasePresenter<State, Event>(dispatcherProvider) {


    override fun initState(): State = State()

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
        }
    }

    private fun createFilter() {
        val errors = validator.validate(currentState)
        if (errors.isEmpty()) {
            val model = FilterModel(
                isBuy = currentState.isBuy,
                min = currentState.min.trim().toDoubleOrNull(),
                max = currentState.max.trim().toDoubleOrNull(),
                fromMerchant = currentState.fromMerchant,
                isRestricted = currentState.isRestricted,
                amount = currentState.amount.toDouble()
            )
            launch {
                repository.insertFilter(model)
            }

        }
        updateState { state ->
            state.copy(errors = errors)
        }
    }


}