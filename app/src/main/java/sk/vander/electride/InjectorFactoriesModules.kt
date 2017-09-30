package sk.vander.electride

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.vander.electride.service.LocationService
import sk.vander.electride.ui.routes.RoutesScreen
import sk.vander.electride.ui.routes.directions.DirectionsScreen
import sk.vander.lib.annotations.ActivityScope
import sk.vander.lib.annotations.ScreenScope
import sk.vander.lib.annotations.ServiceScope
import sk.vander.lib.ui.screen.CoordinatorModule

/**
 * @author marian on 21.9.2017.
 */
object InjectorFactoriesModules {

  @Module
  abstract class Screens {
    @ScreenScope @ContributesAndroidInjector()
    abstract fun contributeRoutesScreen(): RoutesScreen

//    @ScreenScope @ContributesAndroidInjector()
//    abstract fun contributeRouteDetailScreen(): RouteDetailScreen

    @ScreenScope @ContributesAndroidInjector()
    abstract fun contributeDirectionsScreen(): DirectionsScreen
  }

  @Module
  abstract class Activities {
    @ActivityScope @ContributesAndroidInjector(modules = arrayOf(ActivityModules::class))
    abstract fun contributeMainActivity(): MainActivity
  }

  @Module(includes = arrayOf(
      CoordinatorModule::class,
      Screens::class
  ))
  abstract class ActivityModules

  @Module
  abstract class Services {
    @ServiceScope @ContributesAndroidInjector
    abstract fun contributeLocationService(): LocationService
  }
}