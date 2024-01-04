package dev.anvith.binanceninja.data.remote

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.domain.models.FilterModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
actual class PeriodicScheduler(
    private val dispatcherProvider: DispatcherProvider,
    private val filterRepository: FilterRepository,
) : Initializer {
    private var filters: List<FilterModel> = emptyList()
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcherProvider.io() + CoroutineExceptionHandler { _, exception ->
            logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
        })

    override fun initialize() {
        scope.launch {
            filterRepository.getFilters().collectLatest {
                filters = it
            }
        }
    }

}