import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.lyricist.strings
import dev.anvith.binanceninja.App
import dev.anvith.binanceninja.di.AppComponent
import dev.anvith.binanceninja.di.HomeComponent
import dev.anvith.binanceninja.di.create
import me.tatarka.inject.annotations.Inject

typealias HomeApplication = () -> Unit

fun main() {
    val component = HomeComponent::class.create(AppComponent::class.create())
    component.homeFactory()
}

@Inject
fun HomeApplication(app: App) {
    application {
        Window(onCloseRequest = ::exitApplication, title = strings.appName) {
            app()
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    //App()
}