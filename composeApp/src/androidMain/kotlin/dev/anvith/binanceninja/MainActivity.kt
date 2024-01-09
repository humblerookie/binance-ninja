package dev.anvith.binanceninja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dev.anvith.binanceninja.di.ActivityComponent
import dev.anvith.binanceninja.di.create
import dev.anvith.binanceninja.features.ui.core.PermissionHandler


class MainActivity : ComponentActivity() {
    private lateinit var permissionHandler: PermissionHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val activityComponent = ActivityComponent::class.create(
            activity = this, appComponent = (applicationContext as BinanceApplication).appComponent
        )
        permissionHandler = activityComponent.permissionHandler
        permissionHandler.updateActivity(this)
        setContent {
            activityComponent.app()
        }
        onRetainNonConfigurationInstance()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, grantResults)
    }
}