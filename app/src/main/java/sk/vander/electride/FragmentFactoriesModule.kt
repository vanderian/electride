package sk.vander.electride

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.vander.electride.fragment.RouteNewFragment
import sk.vander.electride.fragment.RoutesFragment
import sk.vander.lib.annotations.FragmentScope

/**
 * @author marian on 6.9.2017.
 */
@Module
abstract class FragmentFactoriesModule {

  @Module(includes = arrayOf(
//      ScreenModule::class,
//      CoordinatorModule::class
  ))
  abstract class FragmentModules

  @FragmentScope @ContributesAndroidInjector()
  abstract fun contributeRoutesFragment(): RoutesFragment

  @FragmentScope @ContributesAndroidInjector()
  abstract fun contributeRouteNewFragment(): RouteNewFragment
}
