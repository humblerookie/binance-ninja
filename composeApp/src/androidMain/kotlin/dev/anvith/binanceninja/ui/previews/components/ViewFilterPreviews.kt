package dev.anvith.binanceninja.ui.previews.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.anvith.binanceninja.core.ui.components.PreviewBox
import dev.anvith.binanceninja.core.ui.data.Constants
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.features.ui.ViewFiltersScreen

class ViewFilterPreviews {

    @Preview
    @Composable
    fun  Preview1()  = PreviewBox{
        val model = FilterModel(
            id=1,
            isBuy = true,
            price = 95.6,
            amount = 10000.0,
            fromMerchant = true,
            isProMerchant = true,
            sourceCurrency = Constants.USDT,
            targetCurrency = Constants.INR,
        )
        ViewFiltersScreen.FilterItem(item = model, onRemove = {})
    }
}