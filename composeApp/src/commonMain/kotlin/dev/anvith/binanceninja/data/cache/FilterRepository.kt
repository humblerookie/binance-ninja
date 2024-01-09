package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.coroutines.asFlow
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.data.remote.PeerToPeerApi
import dev.anvith.binanceninja.data.remote.models.ApiResult.Failure
import dev.anvith.binanceninja.data.remote.models.ApiResult.Success
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.FilterMapper
import dev.anvith.binanceninja.domain.mappers.OrderMapper
import dev.anvith.binanceninja.domain.models.FilterModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class FilterRepository(
    private val queries: FilterQueries,
    private val filterMapper: FilterMapper,
    private val peerApi: PeerToPeerApi,
    private val orderMapper: OrderMapper,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun insertFilter(filter: FilterModel) = withContext(dispatcherProvider.io()) {
        queries.addFilter(filterMapper.toData(filter))
    }

    suspend fun getFilters() = withContext(dispatcherProvider.io()) {
        queries.getAllFilters().asFlow().map { it.executeAsList() }.map { filters ->
            filters.map { filterMapper.toDomain(it) }
        }
    }

    fun getCurrencyFilters(currency: String) =
        queries.getFiltersByCurrency(currency).executeAsList().map { filterMapper.toDomain(it) }

    fun removeFilter(id: Long) {
        queries.removeFilter(id);
    }

    suspend fun getOrders(filter: FilterModel) = withContext(dispatcherProvider.io()) {
        when (val response = peerApi.getOrders(filterMapper.toApiRequest(filter))) {
            is Success -> {
                val orders = response.data.data.map { orderMapper.toDomain(it) }
                val range = when{
                    filter.min != null && filter.max != null -> filter.min..filter.max
                    filter.min != null -> 0.0..filter.min
                    else -> filter.max!!..Double.MAX_VALUE

                }
                Result.success(orders.filter { it.price in range })
            }
            else -> {
                Result.failure((response as Failure).throwable)
            }
        }
    }

}