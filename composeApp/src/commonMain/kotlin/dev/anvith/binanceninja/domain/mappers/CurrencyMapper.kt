package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.data.cache.Currency
import dev.anvith.binanceninja.data.remote.models.CurrencyApiModel
import dev.anvith.binanceninja.domain.models.CurrencyModel
import me.tatarka.inject.annotations.Inject

@Inject
class CurrencyMapper {

    fun toDomain(item: CurrencyApiModel, isFiat: Boolean) = CurrencyModel(
        code = item.currencyCode,
        symbol = item.currencySymbol,
        icon = item.iconUrl,
        country = item.countryCode,
        isFiat = isFiat,
    )

    fun toDomain(item: Currency) = CurrencyModel(
        code = item.code,
        symbol = item.symbol,
        icon = item.icon,
        country = item.country,
        isFiat = item.isFiat,
    )

    fun toData(item: CurrencyApiModel, isFiat: Boolean) = Currency(
        code = item.currencyCode,
        symbol = item.currencySymbol,
        icon = item.iconUrl,
        country = item.countryCode,
        isFiat = isFiat,
    )
}