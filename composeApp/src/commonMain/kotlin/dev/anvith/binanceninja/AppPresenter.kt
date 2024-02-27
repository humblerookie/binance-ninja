package dev.anvith.binanceninja

import cafe.adriel.voyager.navigator.tab.Tab
import dev.anvith.binanceninja.AppContract.Event
import dev.anvith.binanceninja.AppContract.Event.Retry
import dev.anvith.binanceninja.AppContract.Event.SelectCurrency
import dev.anvith.binanceninja.AppContract.State
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.BasePresenter
import dev.anvith.binanceninja.core.ui.presentation.bind
import dev.anvith.binanceninja.data.cache.CurrencyRepository
import dev.anvith.binanceninja.di.ActivityScope
import dev.anvith.binanceninja.domain.mappers.ErrorHandler
import dev.anvith.binanceninja.features.ui.CreateFilterPresenter
import dev.anvith.binanceninja.features.ui.CreateFilterScreen
import dev.anvith.binanceninja.features.ui.ViewFiltersPresenter
import dev.anvith.binanceninja.features.ui.ViewFiltersScreen
import kotlinx.coroutines.flow.collectLatest
import me.tatarka.inject.annotations.Inject

private typealias CreateFilterPresenterFactory = () -> CreateFilterPresenter

private typealias ViewFiltersPresenterFactory = () -> ViewFiltersPresenter

@Inject
@ActivityScope
class AppPresenter(
    dispatcherProvider: DispatcherProvider,
    private val createFilterPresenterFactory: CreateFilterPresenterFactory,
    private val viewFiltersPresenterFactory: ViewFiltersPresenterFactory,
    private val currencyRepository: CurrencyRepository,
    private val errorHandler: ErrorHandler,
) : BasePresenter<State, Event>(dispatcherProvider) {
    private lateinit var children: IList<Tab>

    override fun initState() = State()

    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        launch {
            val userCurrency = currencyRepository.getUserCurrency()
            updateState {
                it.copy(
                    userCurrency = userCurrency,
                    errorMessage = null,
                    isLoading = true
                )
            }
            currencyRepository
                .getAllFiatCurrencies(forceRefresh = true) { error ->
                    logE("Error While fetching Fiat currencies", throwable = error)
                    updateState {
                        it.copy(
                            errorMessage = errorHandler.getMessage(error),
                            isLoading = false
                        )
                    }
                }
                .collectLatest {
                    updateState { state ->
                        state.copy(
                            currencies = it.lock(),
                            userCurrency = it.firstOrNull { it.isSelected }
                                ?: it.firstOrNull { it.code == userCurrency.code },
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun getChildren(): IList<Tab> {
        if (!::children.isInitialized) {
            children =
                listOf(
                    CreateFilterScreen.bind(createFilterPresenterFactory),
                    ViewFiltersScreen.bind(viewFiltersPresenterFactory)
                )
                    .lock()
        }
        return children
    }

    override fun onEvent(event: Event) {
        when (event) {
            is SelectCurrency -> {
                launch {
                    updateState { it.copy(userCurrency = event.currency) }
                    currencyRepository.saveUserCurrency(event.currency)
                }
            }

            Retry -> fetchCurrencies()
        }
    }
}
