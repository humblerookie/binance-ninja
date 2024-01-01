package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.coroutines.asFlow
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.FilterMapper
import dev.anvith.binanceninja.domain.models.FilterModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class FilterRepository(
    private val queries: FilterQueries,
    private val mapper: FilterMapper,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun insertFilter(filter: FilterModel) = withContext(dispatcherProvider.io()) {
        queries.addFilter(mapper.toData(filter))
    }

    suspend fun getFilters() = withContext(dispatcherProvider.io()) {
        queries.getAllFilters().asFlow().map { it.executeAsList() }.map { filters ->
            filters.map { mapper.toDomain(it) }
        }
    }

    fun getCurrencyFilters(currency: String) =
        queries.getFiltersByCurrency(currency).executeAsList().map { mapper.toDomain(it) }

    fun removeFilter(id: Long) {
        queries.removeFilter(id);
    }

}