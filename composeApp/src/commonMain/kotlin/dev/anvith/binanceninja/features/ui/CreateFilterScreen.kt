package dev.anvith.binanceninja.features.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.anvith.binanceninja.core.ui.components.AppSwitch
import dev.anvith.binanceninja.core.ui.components.LocalSnackbarProvider
import dev.anvith.binanceninja.core.ui.components.PrimaryButton
import dev.anvith.binanceninja.core.ui.components.SnackbarProvider
import dev.anvith.binanceninja.core.ui.components.Space
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.PresenterTab
import dev.anvith.binanceninja.core.ui.presentation.SideEffect
import dev.anvith.binanceninja.core.ui.presentation.SideEffect.MiscEffect
import dev.anvith.binanceninja.core.ui.presentation.getPresenter
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Effect.FilterCreationSuccess
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.AMOUNT
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.MAX
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.MIN
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.ActionTypeChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.AmountChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.CreateFilter
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.IsRestricted
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MaxChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MinChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


object CreateFilterScreen : PresenterTab() {
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
        val presenter: CreateFilterPresenter = getPresenter()
        val dispatch = presenter::dispatchEvent
        CreateFilterContent(
            stateFlow = presenter.state,
            events = presenter.viewEvents,
            onPrimaryActionChanged = {
                dispatch(ActionTypeChanged(it))
            }, onMinChanged = {
                dispatch(MinChanged(it))
            }, onMaxChanged = {
                dispatch(MaxChanged(it))
            }, onAmountChanged = {
                dispatch(AmountChanged(it))
            }, onMerchantOptionChanged = {
                dispatch(FromMerchant(it))
            }, onRestrictedOptionChanged = {
                dispatch(IsRestricted(it))
            }, onCreateFilter = {
                dispatch(CreateFilter)
            })
    }

    @Composable
    fun CreateFilterContent(
        stateFlow: StateFlow<State>,
        events: Flow<SideEffect>,
        onPrimaryActionChanged: (Boolean) -> Unit,
        onMinChanged: (TextFieldValue) -> Unit,
        onMaxChanged: (TextFieldValue) -> Unit,
        onAmountChanged: (TextFieldValue) -> Unit,
        onMerchantOptionChanged: (Boolean) -> Unit,
        onRestrictedOptionChanged: (Boolean) -> Unit,
        onCreateFilter: () -> Unit
    ) {

        val state by stateFlow.collectAsState()
        val snackbarProvider = LocalSnackbarProvider.current
        val filterSuccess = strings.filterCreationMessage
        LaunchedEffect(Unit) {
            events.collect { sideEffect ->
                onSideEffect(sideEffect, snackbarProvider, filterSuccess)
            }
        }

        Column(
            Modifier.padding(horizontal = Dimens.keyline).verticalScroll(rememberScrollState())
        ) {
            Space(height = Dimens.keyline)
            BuySellAction(state.isBuy, onPrimaryActionChanged)
            Space(height = Dimens.spaceLarge)
            PriceRangeFilter(
                min = state.min,
                max = state.max,
                errors = listOf(state.errors[MIN], state.errors[MAX]).lock(),
                onMinChanged = onMinChanged,
                onMaxChanged = onMaxChanged,
            )
            Space(height = Dimens.spaceLarge)
            AmountFilter(
                amount = state.amount,
                hasError = state.errors[AMOUNT] ?: false,
                onChanged = onAmountChanged,
                onCreateFilter
            )
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

    private fun onSideEffect(
        sideEffect: SideEffect,
        snackbarProvider: SnackbarProvider,
        filterSuccess: String
    ) {
        when (sideEffect) {
            is MiscEffect<*> -> when (sideEffect.data) {
                FilterCreationSuccess -> {
                    snackbarProvider.showSnack(
                        filterSuccess,
                        null,
                        true
                    ) { _ -> }
                }

            }

            else -> {}
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
        errors: IList<Boolean?>,
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
                PriceRangeInput(label, items[index], callbacks[index], hasError = errors[index])
                if (index != radioOptions.lastIndex) {
                    Space(width = Dimens.keyline)
                }
            }
        }

    }

    @Composable
    private fun AmountFilter(
        amount: TextFieldValue,
        hasError: Boolean,
        onChanged: (TextFieldValue) -> Unit,
        onCreateFilter: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppText.H5(text = strings.selectAmount)
        Row {
            Card(
                modifier = modifier.weight(1f).padding(vertical = Dimens.keyline)
            ) {
                AppText.InputNormal(
                    value = amount,
                    isError = hasError,
                    onValueChange = onChanged,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onCreateFilter()
                    }),
                    modifier = Modifier.padding(Dimens.keyline).fillMaxWidth(),
                    supportingText = {
                        if (hasError) {
                            AppText.Body1(
                                text = strings.errorInvalidInput,
                            )
                        }
                    },
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
        hasError: Boolean? = null
    ) {
        Card(
            modifier = modifier.weight(1f).padding(vertical = Dimens.keyline)
        ) {
            AppText.Body1(text = label, modifier = Modifier.padding(Dimens.spaceSmall))
            AppText.InputNormal(
                value = value,
                isError = hasError ?: false,
                onValueChange = onChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    if (hasError == true) {
                        AppText.Body1(
                            text = strings.errorInvalidInput,
                        )
                    }
                },
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