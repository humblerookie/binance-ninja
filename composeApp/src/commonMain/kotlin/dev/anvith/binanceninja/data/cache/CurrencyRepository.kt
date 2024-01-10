package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.coroutines.asFlow
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.data.remote.PeerToPeerApi
import dev.anvith.binanceninja.data.remote.models.CurrencyApiModel
import dev.anvith.binanceninja.data.remote.models.whenError
import dev.anvith.binanceninja.data.remote.models.whenSuccess
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.CurrencyMapper
import dev.anvith.binanceninja.domain.models.CurrencyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class CurrencyRepository(
    private val queries: CurrencyQueries,
    private val mapper: CurrencyMapper,
    private val api: PeerToPeerApi,
    private val userDataStore: UserDataStore,
    private val dispatcherProvider: DispatcherProvider
) {

    fun saveUserCurrency(currency: CurrencyModel) {
        userDataStore.userCurrency = currency.code
    }

    fun getUserCurrency() = userDataStore.userCurrency
    suspend fun getAllFiatCurrencies(
        forceRefresh: Boolean = false,
        onSyncFailure: (Throwable) -> Unit
    ): Flow<List<CurrencyModel>> =
        withContext(dispatcherProvider.io()) {
            launch {
                var isStale = forceRefresh
                if (!forceRefresh) {
                    val currencies = queries.getAllCurrencies().executeAsList()
                    isStale = currencies.isEmpty()
                }
                if (isStale) {
                    api.getFiatCurrencies().whenSuccess {
                        try {
                            insertCurrencies(isFiat = true, filter = it.data)
                        } catch (e: Exception) {
                            logE(e.toString(), e)
                            onSyncFailure(e)
                        }
                    }.whenError {
                        onSyncFailure(it)
                    }
                }
            }
            queries.getAllCurrencies().asFlow().map { it.executeAsList() }
                .map { it.map(mapper::toDomain) }
        }

    private suspend fun insertCurrencies(
        filter: Iterable<CurrencyApiModel>,
        isFiat: Boolean,
    ) = withContext(dispatcherProvider.io()) {
        launch {
            queries.transaction {
                filter.forEach {
                    queries.addCurrency(mapper.toData(it, isFiat))
                }
            }
        }
    }


    suspend fun getFiatCurrencies() = withContext(dispatcherProvider.io()) {
        queries.getFiatCurrencies().asFlow().map { it.executeAsList() }
            .map { it.map(mapper::toDomain) }
    }

    suspend fun getFiatCurrenciesOrEmpty() = withContext(dispatcherProvider.io()) {
        queries.getAllCurrencies().executeAsList().map(mapper::toDomain)
    }

    suspend fun getCryptoCurrencies() = withContext(dispatcherProvider.io()) {
        queries.getCryptoCurrencies().asFlow().map { it.executeAsList() }
            .map { it.map(mapper::toDomain) }
    }

    suspend fun removeAll() = withContext(dispatcherProvider.io()) {
        queries.removeAll()
    }


}