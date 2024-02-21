package dev.anvith.binanceninja.data.cache

import app.cash.sqldelight.coroutines.asFlow
import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.data.extensions.transact
import dev.anvith.binanceninja.data.remote.PeerToPeerApi
import dev.anvith.binanceninja.data.remote.models.AreaType.P2P
import dev.anvith.binanceninja.data.remote.models.CurrencyApiModel
import dev.anvith.binanceninja.data.remote.models.PeerToPeerConfigRequest
import dev.anvith.binanceninja.data.remote.models.whenError
import dev.anvith.binanceninja.data.remote.models.whenSuccess
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.mappers.CurrencyMapper
import dev.anvith.binanceninja.domain.models.CurrencyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
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

  private val userCurrencyUpdates = MutableStateFlow(defaultUserCurrency())

  suspend fun saveUserCurrency(currency: CurrencyModel) =
    withContext(dispatcherProvider.io()) {
      if (currency.code != userDataStore.userCurrency) {
        userDataStore.userCurrency = currency.code
        userDataStore.userCurrencySymbol = currency.symbol
        queries.transact {
          resetSelections()
          addCurrency(mapper.toData(currency.copy(isSelected = true)))
        }
        userCurrencyUpdates.update { currency }
        fetchRemoteCryptoCurrencies { logE(it.toString(), it) }
      }
    }

  suspend fun getUserCurrency() =
    withContext(dispatcherProvider.io()) {
      queries.getDefaultFiatCurrency().executeAsOneOrNull()?.let(mapper::toDomain)
        ?: defaultUserCurrency()
    }

  fun userCurrencyUpdates() = userCurrencyUpdates.asStateFlow().onStart { emit(getUserCurrency()) }

  private fun defaultUserCurrency() =
    CurrencyModel(
      code = userDataStore.userCurrency,
      symbol = userDataStore.userCurrencySymbol,
      icon = null,
      country = null,
      isFiat = true,
      isSelected = true
    )

  suspend fun getAllFiatCurrencies(
    forceRefresh: Boolean = false,
    onSyncFailure: (Throwable) -> Unit
  ): Flow<List<CurrencyModel>> =
    withContext(dispatcherProvider.io()) {
      launch {
        var isStale = forceRefresh
        if (!forceRefresh) {
          val currencies = queries.getFiatCurrencies().executeAsList()
          isStale = currencies.isEmpty()
        }
        if (isStale) {
          api
            .getFiatCurrencies()
            .whenSuccess {
              try {
                insertCurrencies(isFiat = true, filter = it.data)
              } catch (e: Exception) {
                logE(e.toString(), e)
                onSyncFailure(e)
              }
            }
            .whenError { onSyncFailure(it) }
        }
      }
      queries
        .getFiatCurrencies()
        .asFlow()
        .map { it.executeAsList() }
        .map { it.map(mapper::toDomain) }
    }

  suspend fun getAllCryptoCurrencies(
    forceRefresh: Boolean = false,
    onSyncFailure: (Throwable) -> Unit
  ): Flow<List<CurrencyModel>> =
    withContext(dispatcherProvider.io()) {
      launch {
        var isStale = forceRefresh
        if (!forceRefresh) {
          val currencies = queries.getCryptoCurrencies().executeAsList()
          isStale = currencies.isEmpty()
        }
        if (isStale) {
          fetchRemoteCryptoCurrencies(onSyncFailure)
        }
      }
      queries
        .getCryptoCurrencies()
        .asFlow()
        .map { it.executeAsList() }
        .map { it.map(mapper::toDomain) }
    }

  suspend fun getCryptoCurrenciesForFiat(
    forceRefresh: Boolean = false,
    onSyncFailure: (Throwable) -> Unit,
    fiat: String? = userDataStore.userCurrency
  ): Flow<List<CurrencyModel>> =
    withContext(dispatcherProvider.io()) {
      launch {
        var isStale = forceRefresh
        if (!forceRefresh) {
          val currencies = queries.getCryptoCurrenciesForFiat(fiat).executeAsList()
          isStale = currencies.isEmpty()
        }
        if (isStale) {
          fetchRemoteCryptoCurrencies(onSyncFailure)
        }
      }
      queries
        .getCryptoCurrenciesForFiat(fiat)
        .asFlow()
        .map { it.executeAsList() }
        .map { it.map(mapper::toDomain) }
    }

  private suspend fun fetchRemoteCryptoCurrencies(onSyncFailure: (Throwable) -> Unit) {
    val userCurrency = getUserCurrency().code
    api
      .getCryptoCurrencies(PeerToPeerConfigRequest(fiat = userCurrency))
      .whenSuccess { response ->
        try {
          val currencies =
            response.data.areas
              .first { it.area == P2P }
              .tradeSides
              .flatMap { it.assets }
              .toSet()
              .map(mapper::toApiModel)
          insertCurrencies(isFiat = false, filter = currencies, userCurrency = userCurrency)
        } catch (e: Exception) {
          logE(e.toString(), e)
          onSyncFailure(e)
        }
      }
      .whenError { onSyncFailure(it) }
  }

  private suspend fun insertCurrencies(
    filter: Iterable<CurrencyApiModel>,
    isFiat: Boolean,
    userCurrency: String? = null
  ) =
    withContext(dispatcherProvider.io()) {
      launch {
        val selectedCurrency = getUserCurrency()
        queries.transact {
          filter.forEach {
            addCurrency(
              mapper.toData(
                item = it,
                isFiat = isFiat,
                isSelected = it.currencyCode == selectedCurrency.code,
                userCurrency = userCurrency
              )
            )
          }
        }
      }
    }

  suspend fun getFiatCurrenciesOrEmpty() =
    withContext(dispatcherProvider.io()) {
      queries.getAllCurrencies().executeAsList().map(mapper::toDomain)
    }

  suspend fun getAllCurrencies(): Flow<List<CurrencyModel>> =
    withContext(dispatcherProvider.io()) {
      queries
        .getAllCurrencies()
        .asFlow()
        .map { it.executeAsList() }
        .map { it.map(mapper::toDomain) }
    }

  suspend fun removeAll() = withContext(dispatcherProvider.io()) { queries.removeAll() }
}
