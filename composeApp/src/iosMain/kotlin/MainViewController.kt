import androidx.compose.ui.window.ComposeUIViewController
import dev.anvith.binanceninja.App
import me.tatarka.inject.annotations.Inject
import platform.UIKit.UIViewController

typealias MainViewController = () -> UIViewController

@Inject fun MainViewController(app: App) = ComposeUIViewController { app.invoke() }
