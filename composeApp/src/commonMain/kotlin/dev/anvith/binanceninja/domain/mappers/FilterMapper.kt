package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.core.ui.data.formatPrecision
import dev.anvith.binanceninja.data.cache.Filter
import dev.anvith.binanceninja.data.remote.models.PeerToPeerRequest
import dev.anvith.binanceninja.data.remote.models.Publisher.Merchant
import dev.anvith.binanceninja.data.remote.models.TradeType.Buy
import dev.anvith.binanceninja.data.remote.models.TradeType.Sell
import dev.anvith.binanceninja.domain.models.CurrencyModel
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.domain.models.NotificationModel
import me.tatarka.inject.annotations.Inject


@Inject
class FilterMapper {
    fun toDomain(item: Filter): FilterModel {
        return FilterModel(
            id = item.id,
            isBuy = item.isBuy,
            price = (if (item.isBuy) item.max else item.min)!!,
            fromMerchant = item.fromMerchant,
            isProMerchant = item.isProMerchant,
            amount = item.amount,
            sourceCurrency = item.sourceCurrency,
            targetCurrency = item.targetCurrency
        )
    }

    fun toData(item: FilterModel): Filter {
        return Filter(
            id = item.id,
            isBuy = item.isBuy,
            min = if (!item.isBuy) item.price else null,
            max = if (item.isBuy) item.price else null,
            fromMerchant = item.fromMerchant,
            isProMerchant = item.isProMerchant,
            amount = item.amount,
            sourceCurrency = item.sourceCurrency,
            targetCurrency = item.targetCurrency
        )
    }

    fun toApiRequest(item: FilterModel): PeerToPeerRequest {
        return PeerToPeerRequest(
            asset = item.sourceCurrency,
            tradeType = if (item.isBuy) Buy else Sell,
            fiat = item.targetCurrency,
            publisherType = if (item.fromMerchant) Merchant else null,
            amount = item.amount,
            proMerchantAds = item.isProMerchant
        )
    }

    fun toNotification(item: FilterModel, count: Int, currencies: Map<String, CurrencyModel>): NotificationModel {
        println(currencies)
        println(item)
        return NotificationModel(
            title = "$count Order${if (count > 1) "s" else ""} Matched",
            message = item.run {
                val symbol =currencies[targetCurrency]?.symbol?:targetCurrency
                "${if (isBuy) "Buying" else "Selling"} $symbol${
                    formatPrecision(
                        item.amount
                    )
                } at a price of $symbol${
                    formatPrecision(
                        item.price
                    )
                }"
            }
        )
    }
}