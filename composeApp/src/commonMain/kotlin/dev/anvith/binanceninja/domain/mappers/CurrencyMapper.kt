package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.data.cache.Currency
import dev.anvith.binanceninja.data.remote.models.CurrencyApiModel
import dev.anvith.binanceninja.data.remote.models.PeerToPeerAsset
import dev.anvith.binanceninja.domain.models.CurrencyModel
import me.tatarka.inject.annotations.Inject

@Inject
class CurrencyMapper {

    fun toDomain(item: CurrencyApiModel, isFiat: Boolean, isSelected: Boolean) =
        CurrencyModel(
            code = item.currencyCode,
            symbol = item.currencySymbol,
            icon = item.iconUrl,
            country = item.countryCode,
            isFiat = isFiat,
            isSelected = isSelected,
        )

    fun toApiModel(item: PeerToPeerAsset) = CurrencyApiModel(
        currencyCode = item.asset,
        currencySymbol = item.asset,
        iconUrl = item.iconUrl,
        countryCode = "",
    )

    fun toDomain(item: Currency) =
        CurrencyModel(
            code = item.code,
            symbol = item.symbol,
            icon = item.icon,
            country = item.country,
            isFiat = item.isFiat,
            isSelected = item.isSelected,
            availableFor = item.availableFor
        )

    fun toData(item: CurrencyApiModel, isFiat: Boolean, isSelected: Boolean, userCurrency:String?) =
        Currency(
            code = item.currencyCode,
            symbol = item.currencySymbol,
            icon = item.iconUrl,
            country = item.countryCode,
            isFiat = isFiat,
            isSelected = isSelected,
            availableFor = userCurrency
        )

    fun toData(item: CurrencyModel) = Currency(
        code = item.code,
        symbol = item.symbol,
        icon = item.icon,
        country = item.country,
        isFiat = item.isFiat,
        isSelected = item.isSelected,
        availableFor = item.availableFor
    )
}
