package dev.anvith.binanceninja.data.cache

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.FilterMapper
import dev.anvith.binanceninja.domain.models.FilterModel
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
        queries.getAllFilters().executeAsList().map { mapper.toDomain(it) }
    }

    fun getCurrencyFilters(currency: String) =
        queries.getFiltersByCurrency(currency).executeAsList().map { mapper.toDomain(it) }

    fun removeFilter(id: Long) {
        queries.removeFilter(id);
    }

}