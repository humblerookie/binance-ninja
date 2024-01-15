package dev.anvith.binanceninja.di

import androidx.activity.ComponentActivity
import dev.anvith.binanceninja.App
import dev.anvith.binanceninja.features.ui.core.PermissionHandler
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ActivityScope
abstract class ActivityComponent(
    @get:Provides val activity: ComponentActivity,
    @Component val appComponent: AppComponent
) {
    abstract val app: App


    abstract val permissionHandler: PermissionHandler
}
