package dev.anvith.binanceninja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dev.anvith.binanceninja.di.ActivityComponent
import dev.anvith.binanceninja.di.create

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val activityComponent = ActivityComponent::class.create(
            activity = this,
            appComponent = (applicationContext as BinanceApplication).appComponent
        )
        setContent {
            activityComponent.app()
        }
    }
}