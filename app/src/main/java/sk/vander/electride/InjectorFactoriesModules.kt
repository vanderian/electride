package sk.vander.electride

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.vander.electride.service.LocationService
import sk.vander.electride.ui.routes.RouteDetailFragment
import sk.vander.electride.ui.routes.RoutesFragment
import sk.vander.lib.annotations.ActivityScope
import sk.vander.lib.annotations.FragmentScope
import sk.vander.lib.annotations.ServiceScope
import sk.vander.lib.ui.screen.CoordinatorModule

/**
 * @author marian on 21.9.2017.
 */
object InjectorFactoriesModules {

  @Module
  abstract class Fragments {
    @FragmentScope @ContributesAndroidInjector()
    abstract fun contributeRoutesFragment(): RoutesFragment

    @FragmentScope @ContributesAndroidInjector()
    abstract fun contributeRouteDetailFragment(): RouteDetailFragment
  }

  @Module
  abstract class Activities {
    @ActivityScope @ContributesAndroidInjector(modules = arrayOf(ActivityModules::class))
    abstract fun contributeMainActivity(): MainActivity
  }

  @Module(includes = arrayOf(
      CoordinatorModule::class,
      Fragments::class
  ))
  abstract class ActivityModules

  @Module
  abstract class Services {
    @ServiceScope @ContributesAndroidInjector
    abstract fun contributeLocationService(): LocationService
  }
}