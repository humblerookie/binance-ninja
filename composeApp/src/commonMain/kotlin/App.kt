import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import dev.anvith.binanceninja.features.ui.CreateFilterScreen
import dev.anvith.binanceninja.features.ui.ViewFiltersScreen
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.BinanceNinjaTheme
import dev.anvith.binanceninja.core.ui.theme.TextModifier
import dev.anvith.binanceninja.core.ui.theme.ThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    BinanceNinjaTheme {
        ProvideStrings {
            TabNavigator(CreateFilterScreen) {
                Scaffold(
                    content = {
                        Box(modifier = Modifier.padding(it)) {
                            CurrentTab()
                        }
                    },
                    topBar = {
                        TopAppBar(
                            title = {
                                AppText.H3(
                                    text = strings.appName,
                                    textModifier = TextModifier.color(ThemeColors.onPrimary)
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColors.primary)
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            TabNavigationItem(CreateFilterScreen)
                            TabNavigationItem(ViewFiltersScreen)
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title,
            )
        })
}