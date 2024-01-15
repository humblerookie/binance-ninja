package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.coroutines.asFlow
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.data.remote.PeerToPeerApi
import dev.anvith.binanceninja.data.remote.models.ApiResult.Failure
import dev.anvith.binanceninja.data.remote.models.ApiResult.Success
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.FilterMapper
import dev.anvith.binanceninja.domain.mappers.OrderMapper
import dev.anvith.binanceninja.domain.models.FilterModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@OptIn(DelicateCoroutinesApi::class)
@Inject
@AppScope
class FilterRepository(
  private val queries: FilterQueries,
  private val ordersQueries: OrdersQueries,
  private val filterMapper: FilterMapper,
  private val peerApi: PeerToPeerApi,
  private val orderMapper: OrderMapper,
  private val dispatcherProvider: DispatcherProvider
) {
  private var currentOrders: Map<String, Boolean> = emptyMap()

  init {
    GlobalScope.launch(dispatcherProvider.io()) {
      ordersQueries
        .getAllOrders()
        .asFlow()
        .map { it.executeAsList() }
        .collectLatest { orders -> currentOrders = orders.associateBy({ it }, { true }) }
    }
  }

  suspend fun insertFilter(filter: FilterModel) =
    withContext(dispatcherProvider.io()) { queries.addFilter(filterMapper.toData(filter)) }

  suspend fun getFilters() =
    withContext(dispatcherProvider.io()) {
      queries
        .getAllFilters()
        .asFlow()
        .map { it.executeAsList() }
        .map { filters -> filters.map { filterMapper.toDomain(it) } }
    }

  suspend fun resetOrders() = withContext(dispatcherProvider.io()) { ordersQueries.deleteAll() }

  fun getCurrencyFilters(currency: String) =
    queries.getFiltersByCurrency(currency).executeAsList().map { filterMapper.toDomain(it) }

  fun removeFilter(id: Long) {
    queries.removeFilter(id)
  }

  suspend fun getOrders(filter: FilterModel) =
    withContext(dispatcherProvider.io()) {
      when (val response = peerApi.getOrders(filterMapper.toApiRequest(filter))) {
        is Success -> {
          val orders = response.data.data.map { orderMapper.toDomain(it) }
          val filteredOrders =
            if (filter.isBuy) {
                orders.filter { it.price <= filter.price }
              } else {
                orders.filter { it.price >= filter.price }
              }
              .filter { !currentOrders.containsKey(it.id) }
          try {
            ordersQueries.transaction { filteredOrders.forEach { ordersQueries.saveOrder(it.id) } }
            Result.success(filteredOrders)
          } catch (e: Exception) {
            logE("Error saving orders ${e.message}")
            Result.failure(e)
          }
        }
        else -> {
          Result.failure((response as Failure).throwable)
        }
      }
    }
}
