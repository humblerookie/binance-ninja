package dev.anvith.binanceninja.features.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.anvith.binanceninja.core.ui.components.Space
import dev.anvith.binanceninja.core.ui.data.Constants.Assets
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.presentation.PresenterTab
import dev.anvith.binanceninja.core.ui.presentation.getPresenter
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.TextModifier
import dev.anvith.binanceninja.core.ui.theme.ThemeColors
import dev.anvith.binanceninja.core.ui.theme.alpha38
import dev.anvith.binanceninja.domain.models.FilterModel
import dev.anvith.binanceninja.features.ui.ViewFiltersContract.Event.RemoveFilter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object ViewFiltersScreen: PresenterTab() {

    override val key = uniqueScreenKey
    override val options: TabOptions
        @Composable
        get() {
            val title = strings.tabViewFilters
            val icon = rememberVectorPainter(image = Icons.Default.List)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon

                )
            }
        }

    @Composable
    override fun Content() {
        val presenter: ViewFiltersPresenter = getPresenter()
        val state by presenter.state.collectAsState()
        when {
            state.isLoading -> {
                Loader()
            }

            state.error != null -> {
                MessageSection(state.error!!)
            }

            state.filters.isEmpty() -> {
                MessageSection(strings.errorNoFilters)
            }

            else -> {
                Filters(state.filters,{
                    presenter.dispatchEvent(RemoveFilter(it))
                })
            }
        }


    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    private fun MessageSection(message: String) {
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
        }
    }

    @Composable
    private fun Loader() {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    @Composable
    private fun Filters(filters: IList<FilterModel>, onRemove:(FilterModel)->Unit) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimens.keyline)) {
            item {
                Space(Dimens.keyline)
            }
            items(filters) { filter ->
                FilterItem(filter,onRemove)
            }
            item {
                Space(Dimens.keyline)
            }
        }
    }


    @Composable
    fun FilterItem(item: FilterModel, onRemove: (FilterModel) -> Unit) {
        Card(
            Modifier
                .padding(horizontal = Dimens.keyline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = Dimens.listBlockNormal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.keyline).weight(1f),
                ) {
                    AppText.Body1(
                        text = strings.actionDynamicsLabel(
                            item.sourceCurrency,
                            item.targetCurrency,
                            item.isBuy,
                            item.amount,
                            ThemeColors.onSurface.alpha38()
                        ),
                    )
                    Space(height = Dimens.spaceTiny)
                    AppText.Body1(
                        text = strings.priceDynamicsLabel(
                            item.targetCurrency,
                            item.price,
                            ThemeColors.onSurface.alpha38()
                        )
                    )
                    if (item.isProMerchant || item.fromMerchant) {
                        Space(height = Dimens.spaceNormal)
                        Row {
                            if (item.fromMerchant) {
                                Label(strings.labelMerchant, Icons.Filled.CheckCircle)
                                Space(width = Dimens.spaceNormal)
                            }

                            if (item.isProMerchant) {
                                Label(strings.labelProMerchant, Icons.Filled.Lock)
                            }
                        }
                    }

                }
                Space(width = Dimens.spaceSmall)
                DeleteButton { onRemove(item) }
            }
        }
    }

    @Composable
    private fun RowScope.DeleteButton(modifier: Modifier = Modifier, onRemove: () -> Unit) {
        IconButton(
            onClick = onRemove,
            modifier = modifier.size(Dimens.iconSmall).background(
                color = ThemeColors.secondary,
                shape = RoundedCornerShape(
                    topEnd = Dimens.radiusButton,
                    bottomStart = Dimens.radiusButton
                )
            ).padding(Dimens.spaceTiny)
                .align(Alignment.Top)
        ) {
            Icon(
                Icons.Filled.Clear,
                tint = ThemeColors.onPrimary,
                contentDescription = strings.labelRemove,
            )
        }
    }

    @Composable
    private fun Label(label: String, icon: ImageVector) {
        val contentColor = ThemeColors.primary
        Row(
            modifier = Modifier.border(
                width = Dimens.borderSmall,
                color = contentColor,
                shape = RoundedCornerShape(Dimens.radiusHuge)
            ).padding(horizontal = Dimens.spaceNormal, vertical = Dimens.spaceXSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                contentDescription = label,
                Modifier.size(AssistChipDefaults.IconSize),
                tint = contentColor,
            )
            Space(width = Dimens.spaceTiny)
            AppText.Button1(
                label,
                textModifier = TextModifier.color(contentColor)
            )
        }
    }

}