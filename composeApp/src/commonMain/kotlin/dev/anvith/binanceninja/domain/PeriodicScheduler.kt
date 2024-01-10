package dev.anvith.binanceninja.domain

import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.core.ui.data.Constants.PARALLELISM
import dev.anvith.binanceninja.data.NotificationService
import dev.anvith.binanceninja.data.cache.CurrencyRepository
import dev.anvith.binanceninja.data.cache.FilterRepository
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.FilterMapper
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.domain.models.Orders
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class PeriodicScheduler(
    private val dispatcherProvider: DispatcherProvider,
    private val filterRepository: FilterRepository,
    private val notificationService: NotificationService,
    private val platformScheduler: PlatformScheduler,
    private val currencyRepository: CurrencyRepository,
    private val mapper: FilterMapper,
) : Initializer, RequestExecutor {

    private var filters: List<FilterModel> = emptyList()
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcherProvider.io() + CoroutineExceptionHandler { _, exception ->
            logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
        })

    override fun initialize() {
        scope.launch {
            filterRepository.getFilters().collectLatest {
                filters = it
                if (filters.isNotEmpty()) {
                    platformScheduler.schedule(this@PeriodicScheduler)
                } else {
                    platformScheduler.cancel()
                }
            }
        }
    }

    override suspend fun executeRequests(): Boolean {
        if (filters.isNotEmpty()) {
            val requestScope = CoroutineScope(dispatcherProvider.io() + SupervisorJob())
            val downloadJobs = mutableListOf<Pair<FilterModel, Deferred<Result<Orders>>>>()
            val completedJobs = mutableListOf<Pair<FilterModel, Result<Orders>>>()
            val currencies = currencyRepository.getFiatCurrenciesOrEmpty().associateBy { it.code }
            filters.forEach { first ->
                val res = requestScope.async {
                    filterRepository.getOrders(first)
                }
                downloadJobs.add(Pair(first, res))
                if (downloadJobs.size >= PARALLELISM) {
                    executeJobs(downloadJobs, completedJobs)
                }
            }
            executeJobs(downloadJobs, completedJobs)
            requestScope.cancel()
            val matchedOrders = completedJobs.filter {(_,res)-> res.isSuccess  && res.getOrThrow().isNotEmpty()}
            if (matchedOrders.isNotEmpty()) {
                notificationService.notify(matchedOrders.map { it.first to it.second.getOrThrow().size }
                    .map {
                        mapper.toNotification(
                            it.first,
                            it.second,
                            currencies
                        )
                    })
            }
            return matchedOrders.size == filters.size
        } else {
            return true
        }
    }

    private suspend fun executeJobs(
        downloadJobs: MutableList<Pair<FilterModel, Deferred<Result<Orders>>>>,
        completedJobs: MutableList<Pair<FilterModel, Result<Orders>>>
    ) {
        downloadJobs.forEach {
            completedJobs.add(it.first to it.second.await())
        }
        downloadJobs.clear()
    }
}

interface RequestExecutor {
    suspend fun executeRequests(): Boolean
}