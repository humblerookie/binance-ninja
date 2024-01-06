package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.data.remote.models.PeerToPeerOrder
import dev.anvith.binanceninja.data.remote.models.TradeType
import dev.anvith.binanceninja.domain.models.Order
import dev.anvith.binanceninja.domain.models.PaymentMode
import me.tatarka.inject.annotations.Inject

@Inject
class OrderMapper {

    fun toDomain(item: PeerToPeerOrder) = Order(
        id = item.ad?.advNo.orEmpty(),
        advertiser = item.advertiser?.nickName.orEmpty(),
        advertiserId = item.advertiser?.userNo.orEmpty(),
        price = item.ad?.price?.toDoubleOrNull() ?: 0.0,
        currency = item.ad?.fiatUnit.orEmpty(),
        asset = item.ad?.asset.orEmpty(),
        isBuy = item.ad?.tradeType == TradeType.Buy,
        paymentModes = item.ad?.tradeMethods?.map {
            PaymentMode(
                it?.identifier.orEmpty(),
                it?.tradeMethodName.orEmpty()
            )
        } ?: emptyList(),
        minAmount = item.ad?.minSingleTransAmount?.toDoubleOrNull() ?: 0.0,
        maxAmount = item.ad?.maxSingleTransAmount?.toDoubleOrNull() ?: 0.0,
        qty = item.ad?.tradeQty?.toDoubleOrNull() ?: 0.0,
    )
}