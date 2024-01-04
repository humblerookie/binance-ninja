package dev.anvith.binanceninja

import cafe.adriel.voyager.navigator.tab.Tab
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.BasePresenter
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.di.ActivityScope
import dev.anvith.binanceninja.features.ui.CreateFilterPresenter
import dev.anvith.binanceninja.features.ui.CreateFilterScreen
import dev.anvith.binanceninja.features.ui.ViewFiltersPresenter
import dev.anvith.binanceninja.features.ui.ViewFiltersScreen
import dev.anvith.binanceninja.features.ui.validators.CreateFilterValidator
import me.tatarka.inject.annotations.Inject


private typealias CreateFilterPresenterFactory = (
    repository: FilterRepository,
    validator: CreateFilterValidator,
    dispatcherProvider: DispatcherProvider,
) -> CreateFilterPresenter


private typealias ViewFiltersPresenterFactory = (
    repository: FilterRepository,
    dispatcherProvider: DispatcherProvider,
) -> ViewFiltersPresenter


@Inject
@ActivityScope
class AppPresenter(
    private val dispatcherProvider: DispatcherProvider,
    private val createFilterPresenterFactory: CreateFilterPresenterFactory,
    private val viewFiltersPresenterFactory: ViewFiltersPresenterFactory,
    private val createFilterValidator: CreateFilterValidator,
    private val repository: FilterRepository,
) {
    private lateinit var children: IList<Tab>
    fun getChildren(): IList<Tab> {
        if (!::children.isInitialized) {
            children = listOf(
                CreateFilterScreen.also {
                    (BasePresenter.getPresenter(it.key)?:createFilterPresenterFactory(
                        repository,
                        createFilterValidator,
                        dispatcherProvider
                    )).bind(it)
                },
                ViewFiltersScreen.also {
                    (BasePresenter.getPresenter(it.key)?: viewFiltersPresenterFactory(repository, dispatcherProvider)).bind(it)
                }
            ).lock()
        }
        return children
    }
}