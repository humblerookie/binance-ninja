package dev.anvith.binanceninja.features.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.lyricist.strings
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.TextModifier
import dev.anvith.binanceninja.core.ui.theme.ThemeColors
import dev.anvith.binanceninja.core.ui.theme.alpha12
import dev.anvith.binanceninja.domain.models.CurrencyModel

@Composable
fun CurrencyDialog(
    currencies: IList<CurrencyModel>,
    selectedCurrency: CurrencyModel?,
    onClick: (CurrencyModel) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column {
            val listState = rememberLazyListState()
            Row (modifier = Modifier.background(ThemeColors.primary).fillMaxWidth().padding(Dimens.keyline)){
                AppText.H3(strings.selectCurrency, textModifier = TextModifier.color(ThemeColors.onPrimary))
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(ThemeColors.surface),
                state = listState
            ) {
                items(currencies) { currency ->
                    val isSelected = currency.code == selectedCurrency?.code
                    AppText.Button1(
                        text = "${currency.code} (${currency.symbol})",
                        textModifier =
                        TextModifier.color(if (isSelected) ThemeColors.primary else ThemeColors.onSurface),
                        modifier =
                        Modifier.fillMaxWidth()
                            .background(ThemeColors.surface)
                            .clickable {
                                onClick(currency)
                                onDismiss()
                            }
                            .padding(horizontal = Dimens.keyline, vertical = Dimens.keyline)
                    )
                    Divider(color = ThemeColors.onSurface.alpha12())
                }
            }
        }
    }
}