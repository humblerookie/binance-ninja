package dev.anvith.binanceninja.di

import MainViewController
import me.tatarka.inject.annotations.Component

@Component
@ActivityScope
abstract class HomeViewControllerComponent(@Component val applicationComponent: AppComponent) {

  abstract val homeViewControllerFactory: MainViewController
}
