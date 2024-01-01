package dev.anvith.binanceninja.features.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.anvith.binanceninja.core.ui.components.AppSwitch
import dev.anvith.binanceninja.core.ui.components.PrimaryButton
import dev.anvith.binanceninja.core.ui.components.Space
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.ActionTypeChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.AmountChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.CreateFilter
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.IsRestricted
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MaxChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MinChanged


class CreateFilterScreen(
    private val presenter: CreateFilterPresenter
) : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = strings.tabCreateFilter
            val icon = rememberVectorPainter(image = Icons.Default.Create)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        CreateFilterContent(onPrimaryActionChanged = {
            presenter.dispatchEvent(ActionTypeChanged(it))
        }, onMinChanged = {
            presenter.dispatchEvent(MinChanged(it))
        }, onMaxChanged = {
            presenter.dispatchEvent(MaxChanged(it))
        }, onAmountChanged = {
            presenter.dispatchEvent(AmountChanged(it))
        }, onMerchantOptionChanged = {
            presenter.dispatchEvent(FromMerchant(it))
        }, onRestrictedOptionChanged = {
            presenter.dispatchEvent(IsRestricted(it))
        }, onCreateFilter = {
            presenter.dispatchEvent(CreateFilter)
        })
    }

    @Composable
    fun CreateFilterContent(
        onPrimaryActionChanged: (Boolean) -> Unit,
        onMinChanged: (TextFieldValue) -> Unit,
        onMaxChanged: (TextFieldValue) -> Unit,
        onAmountChanged: (TextFieldValue) -> Unit,
        onMerchantOptionChanged: (Boolean) -> Unit,
        onRestrictedOptionChanged: (Boolean) -> Unit,
        onCreateFilter: () -> Unit
    ) {

        val state by presenter.state.collectAsState()

        Column(
            Modifier.padding(horizontal = Dimens.keyline).verticalScroll(rememberScrollState())
        ) {
            Space(height = Dimens.keyline)
            BuySellAction(state.isBuy, onPrimaryActionChanged)
            Space(height = Dimens.spaceLarge)
            PriceRangeFilter(
                state.min,
                state.max,
                onMinChanged = onMinChanged,
                onMaxChanged = onMaxChanged
            )
            Space(height = Dimens.spaceLarge)
            AmountFilter(state.amount, onAmountChanged)
            Space(height = Dimens.spaceLarge)
            MiscOptions(
                fromMerchant = state.fromMerchant,
                isRestricted = state.isRestricted,
                onMerchantOptionChanged = onMerchantOptionChanged,
                onRestrictedOptionChanged = onRestrictedOptionChanged,
            )
            Space(height = Dimens.spaceLarge)
            CreateFilterButton(onCreateFilter)
            Space(height = Dimens.keyline)
        }
    }

    @Composable
    private fun MiscOptions(
        fromMerchant: Boolean,
        isRestricted: Boolean,
        onMerchantOptionChanged: (Boolean) -> Unit,
        onRestrictedOptionChanged: (Boolean) -> Unit,
    ) {
        AppText.H5(text = strings.miscRequirements)
        val labels = strings.miscOptions
        val actions = listOf(onMerchantOptionChanged, onRestrictedOptionChanged)
        val values = listOf(fromMerchant, isRestricted)
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.keyline)
        ) {
            Column(Modifier.padding(vertical = Dimens.spaceNormal)) {
                labels.forEachIndexed { i, it ->
                    MiscOption(it, values[i], actions[i])
                }

            }
        }
    }

    @Composable
    private fun MiscOption(label: String, isChecked: Boolean, onChanged: (Boolean) -> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onChanged,
                enabled = true,
            )
            AppText.H5(text = label)
        }
    }

    @Composable
    private fun PriceRangeFilter(
        min: TextFieldValue,
        max: TextFieldValue,
        onMinChanged: (TextFieldValue) -> Unit,
        onMaxChanged: (TextFieldValue) -> Unit,
    ) {
        val radioOptions = listOf(
            strings.labelGreaterThan, strings.labelLessThan
        )
        val items = listOf(min, max)
        val callbacks = listOf(onMinChanged, onMaxChanged)
        AppText.H5(text = strings.selectPrice)
        Row {
            radioOptions.forEachIndexed { index, label ->
                PriceRangeInput(label, items[index], callbacks[index])
                if (index != radioOptions.lastIndex) {
                    Space(width = Dimens.keyline)
                }
            }
        }

    }

    @Composable
    private fun AmountFilter(
        amount: TextFieldValue, onChanged: (TextFieldValue) -> Unit, modifier: Modifier = Modifier
    ) {
        AppText.H5(text = strings.selectAmount)
        Row {
            Card(
                modifier = modifier.weight(1f).padding(vertical = Dimens.keyline)
            ) {
                AppText.InputNormal(
                    value = amount,
                    onValueChange = onChanged,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.padding(Dimens.keyline).fillMaxWidth()
                )
            }
        }
    }

    @Composable
    private fun RowScope.PriceRangeInput(
        label: String,
        value: TextFieldValue,
        onChanged: (TextFieldValue) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Card(
            modifier = modifier.weight(1f).padding(vertical = Dimens.keyline)
        ) {
            AppText.Body1(text = label, modifier = Modifier.padding(Dimens.spaceSmall))
            AppText.InputNormal(
                value = value,
                onValueChange = onChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.padding(Dimens.keyline)
            )
        }
    }

    @Composable
    private fun CreateFilterButton(onClick: () -> Unit) {
        PrimaryButton(
            label = strings.actionCreateFilter,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    private fun BuySellAction(isBuy: Boolean, onCheckChanged: (isChecked: Boolean) -> Unit) {
        AppText.H5(text = strings.buySellPrompt)
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.keyline)
        ) {
            Space(height = Dimens.keyline)
            AppSwitch(
                onLabel = strings.actionBuy,
                offLabel = strings.actionSell,
                isChecked = isBuy,
                modifier = Modifier.padding(horizontal = Dimens.keyline),
                onCheckChanged = onCheckChanged
            )
            Spacer(modifier = Modifier.height(Dimens.spaceNormal))
        }
    }


}