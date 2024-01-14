package dev.anvith.binanceninja.di

import HomeApplication
import me.tatarka.inject.annotations.Component

@Component
@ActivityScope
abstract class HomeComponent(@Component val applicationComponent: AppComponent) {

  abstract val homeFactory: HomeApplication
}
