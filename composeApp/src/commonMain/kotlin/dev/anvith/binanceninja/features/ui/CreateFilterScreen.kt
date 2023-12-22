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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.anvith.binanceninja.core.ui.components.AppSwitch
import dev.anvith.binanceninja.core.ui.components.PrimaryButton
import dev.anvith.binanceninja.core.ui.components.Space
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens


object CreateFilterScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = strings.tabCreateFilter
            val icon = rememberVectorPainter(image = Icons.Default.Create)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon

                )
            }
        }

    @Composable
    override fun Content() {
        Column(Modifier.padding(Dimens.keyline).verticalScroll(rememberScrollState())) {
            BuySellAction()
            Space(height = Dimens.spaceLarge)
            AmountFilter()
            Space(height = Dimens.spaceLarge)
            MiscOptions()
            CreateFilterButton()
            Space(height = Dimens.keyline)
        }
    }


    @Composable
    private fun MiscOptions() {
        AppText.H5(text = strings.miscRequirements)
        val labels = strings.miscOptions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.keyline)
        ) {
            Column(Modifier.padding(vertical = Dimens.spaceNormal)) {
                labels.forEach {
                    MiscOption(it)
                }

            }
        }
    }

    @Composable
    private fun MiscOption(label: String) {
        var isChecked by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                enabled = true,
            )
            AppText.H5(text = label)
        }
    }

    @Composable
    private fun AmountFilter() {
        val radioOptions = listOf(
            strings.labelGreaterThan,
            strings.labelLessThan
        )
        AppText.H5(text = strings.selectAmount)
        Row {
            radioOptions.forEachIndexed { index, label ->
                AmountInput(label)
                if (index != radioOptions.lastIndex) {
                    Space(width = Dimens.keyline)
                }
            }
        }

    }

    @Composable
    private fun RowScope.AmountInput(label: String, modifier: Modifier = Modifier) {
        var content by remember { mutableStateOf("") }
        Card(
            modifier = modifier
                .weight(1f)
                .padding(vertical = Dimens.keyline)
        ) {
            AppText.Body1(text = label, modifier = Modifier.padding(Dimens.spaceSmall))
            AppText.InputNormal(
                value = content,
                onValueChange = {
                    content = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.padding(Dimens.keyline)
            )
        }
    }

    @Composable
    private fun CreateFilterButton() {
        PrimaryButton(
            label = strings.actionCreateFilter,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    private fun BuySellAction() {
        AppText.H5(text = strings.buySellPrompt)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.keyline)
        ) {
            Space(height = Dimens.keyline)
            var checkedState by remember { mutableStateOf(true) }
            AppSwitch(
                onLabel = strings.actionBuy,
                offLabel = strings.actionSell,
                isChecked = checkedState,
                modifier = Modifier.padding(horizontal = Dimens.keyline),
                onCheckChanged = { checkedState = !checkedState }
            )
            Spacer(modifier = Modifier.height(Dimens.spaceNormal))
        }
    }


}