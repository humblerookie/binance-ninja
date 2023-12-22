package dev.anvith.binanceninja.features.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.anvith.binanceninja.core.ui.theme.AppText

object ViewFiltersScreen : Tab {
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
        AppText.H1(text = "View Filter")
    }

}