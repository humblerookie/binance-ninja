package dev.anvith.binanceninja.features.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.anvith.binanceninja.core.ui.components.AppSwitch
import dev.anvith.binanceninja.core.ui.components.LocalSnackbarProvider
import dev.anvith.binanceninja.core.ui.components.PrimaryButton
import dev.anvith.binanceninja.core.ui.components.SnackbarProvider
import dev.anvith.binanceninja.core.ui.components.Space
import dev.anvith.binanceninja.core.ui.data.Constants.Assets
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.core.ui.presentation.PresenterTab
import dev.anvith.binanceninja.core.ui.presentation.SideEffect
import dev.anvith.binanceninja.core.ui.presentation.SideEffect.MiscEffect
import dev.anvith.binanceninja.core.ui.presentation.getPresenter
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.TextModifier
import dev.anvith.binanceninja.core.ui.theme.ThemeColors
import dev.anvith.binanceninja.core.ui.theme.alpha12
import dev.anvith.binanceninja.domain.models.CurrencyModel
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Effect.FilterCreationSuccess
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.AMOUNT
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.CURRENCY
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.MAX
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget.MIN
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.ActionTypeChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.AmountChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.CreateFilter
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.FromMerchant
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.IsRestricted
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MaxChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.MinChanged
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.Retry
import dev.anvith.binanceninja.features.ui.CreateFilterContract.Event.SelectCurrency
import dev.anvith.binanceninja.features.ui.CreateFilterContract.State
import dev.anvith.binanceninja.features.ui.extensions.localized
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


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
            }, onCurrencyChanged = {
                dispatch(SelectCurrency(it))
            }, onRetry = {
                dispatch(Retry)
            })
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    private fun ErrorSection(message: String, onRetry: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize().padding(Dimens.keyline),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Assets.EMPTY),
                null,
                contentScale = ContentScale.Inside,
                modifier = Modifier.size(Dimens.iconNormal),
                colorFilter = ColorFilter.tint(ThemeColors.onSurface)
            )
            Space(height = Dimens.spaceXLarge)
            AppText.Body1(
                text = message,
            )
            Space(height = Dimens.spaceLarge)
            PrimaryButton(
                label = strings.actionRetry,
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun Loader() {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
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
        onCreateFilter: () -> Unit,
        onCurrencyChanged: (CurrencyModel) -> Unit,
        onRetry: () -> Unit
    ) {

        val state by stateFlow.collectAsState()
        val snackbarProvider = LocalSnackbarProvider.current
        val filterSuccess = strings.filterCreationMessage
        LaunchedEffect(Unit) {
            events.collect { sideEffect ->
                onSideEffect(sideEffect, snackbarProvider, filterSuccess)
            }
        }

        when {
            state.isLoading -> Loader()
            state.errorMessage != null -> ErrorSection(state.errorMessage!!.localized(), onRetry)
            else -> UserInputForm(
                state = state,
                onPrimaryActionChanged = onPrimaryActionChanged,
                onMinChanged = onMinChanged,
                onMaxChanged = onMaxChanged,
                onAmountChanged = onAmountChanged,
                onMerchantOptionChanged = onMerchantOptionChanged,
                onRestrictedOptionChanged = onRestrictedOptionChanged,
                onCreateFilter = onCreateFilter,
                onCurrencyChanged = onCurrencyChanged
            )
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
    private fun UserInputForm(
        state: State,
        onPrimaryActionChanged: (Boolean) -> Unit,
        onMinChanged: (TextFieldValue) -> Unit,
        onMaxChanged: (TextFieldValue) -> Unit,
        onAmountChanged: (TextFieldValue) -> Unit,
        onMerchantOptionChanged: (Boolean) -> Unit,
        onRestrictedOptionChanged: (Boolean) -> Unit,
        onCreateFilter: () -> Unit,
        onCurrencyChanged: (CurrencyModel) -> Unit
    ) {
        Column(
            Modifier.padding(horizontal = Dimens.keyline).verticalScroll(rememberScrollState())
        ) {
            Space(height = Dimens.keyline)
            BuySellAction(isBuy = state.isBuy, onCheckChanged = onPrimaryActionChanged)
            CurrencySelector(
                currencies = state.currencies,
                selectedCurrency = state.selectedCurrency,
                hasError = state.validationErrors[CURRENCY] ?: false,
                onClick = onCurrencyChanged
            )
            Space(height = Dimens.spaceLarge)
            PriceRangeFilter(
                min = state.min,
                max = state.max,
                errors = listOf(state.validationErrors[MIN], state.validationErrors[MAX]).lock(),
                onMinChanged = onMinChanged,
                onMaxChanged = onMaxChanged,
            )
            Space(height = Dimens.spaceLarge)
            AmountFilter(
                amount = state.amount,
                hasError = state.validationErrors[AMOUNT] ?: false,
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CurrencySelector(
        currencies: IList<CurrencyModel>,
        selectedCurrency: CurrencyModel?,
        onClick: (CurrencyModel) -> Unit,
        hasError: Boolean
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        AppText.H5(text = strings.selectCurrency)
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.keyline)
        ) {
            Box(modifier = Modifier.height(IntrinsicSize.Min)) {
                AppText.InputNormal(
                    value = TextFieldValue(selectedCurrency?.code.orEmpty()),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.keyline)
                        .padding(top = Dimens.keyline).clickable {
                            expanded = !expanded
                        },
                    readOnly = true,
                    onValueChange = {},
                    isError = hasError,
                    supportingText = {
                        if (hasError) {
                            AppText.Body1(text = strings.errorInvalidInput)
                        }
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable(enabled = true) { expanded = true },
                    color = Color.Transparent,
                ) {}
            }
            if (expanded) {
                CurrencyDialog(currencies, selectedCurrency, onClick) {
                    expanded = false
                }
            }
        }
    }

    @Composable
    private fun CurrencyDialog(
        currencies: IList<CurrencyModel>,
        selectedCurrency: CurrencyModel?,
        onClick: (CurrencyModel) -> Unit,
        onDismiss: () -> Unit,
    ) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxWidth().background(ThemeColors.surface),
                state = listState
            ) {
                items(currencies) { currency ->
                    val isSelected = currency == selectedCurrency
                    AppText.Button1(
                        text = "${currency.code} (${currency.symbol})",
                        textModifier = TextModifier.color(if (isSelected) ThemeColors.onPrimary else ThemeColors.onSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isSelected) ThemeColors.primary else ThemeColors.surface)
                            .clickable {
                                onClick(currency)
                                onDismiss()
                            }
                            .padding(
                                horizontal = Dimens.keyline,
                                vertical = Dimens.keyline
                            )

                    )
                    Divider(color = Color.White.alpha12())
                }
            }
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