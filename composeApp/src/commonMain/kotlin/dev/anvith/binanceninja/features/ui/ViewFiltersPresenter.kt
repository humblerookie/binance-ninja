package dev.anvith.binanceninja.features.ui

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.BasePresenter
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.features.ui.ViewFiltersContract.Event
import dev.anvith.binanceninja.features.ui.ViewFiltersContract.Event.RemoveFilter
import dev.anvith.binanceninja.features.ui.ViewFiltersContract.State
import me.tatarka.inject.annotations.Inject

@Inject
class ViewFiltersPresenter(
    private val repository: FilterRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BasePresenter<State, Event>(dispatcherProvider) {

    init {
        subscribeToFilters()
    }

    private fun subscribeToFilters() {
        launch(dispatcherProvider.io()) {
            updateState {
                it.copy(isLoading = true)
            }
            val filters = repository.getFilters()
            updateState { state ->
                state.copy(filters = filters.lock(), isLoading = false)
            }
        }
    }

    override fun initState(): State = State()

    override fun onEvent(event: Event) {
        when (event) {
            is RemoveFilter -> {
                launch(dispatcherProvider.io()) {
                    repository.removeFilter(event.filter.id)
                    updateState {
                        it.copy(filters = (it.filters - event.filter).lock())
                    }
                }
            }
        }
    }


}