package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.data.cache.Filter
import dev.anvith.binanceninja.domain.models.FilterModel
import me.tatarka.inject.annotations.Inject


@Inject
class FilterMapper {
    fun toDomain(item: Filter): FilterModel {
        return FilterModel(
            id = item.id,
            isBuy = item.isBuy,
            min = item.min,
            max = item.max,
            fromMerchant = item.fromMerchant,
            isRestricted = item.isRestricted,
            amount = item.amount,
            sourceCurrency = item.sourceCurrency,
            targetCurrency = item.targetCurrency
        )
    }

    fun toData(item: FilterModel): Filter {
        return Filter(
            id = item.id,
            isBuy = item.isBuy,
            min = item.min,
            max = item.max,
            fromMerchant = item.fromMerchant,
            isRestricted = item.isRestricted,
            amount = item.amount,
            sourceCurrency = item.sourceCurrency,
            targetCurrency = item.targetCurrency
        )
    }
}