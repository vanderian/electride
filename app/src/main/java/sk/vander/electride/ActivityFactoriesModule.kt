package sk.vander.electride

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.vander.lib.annotations.ActivityScope
import sk.vander.lib.ui.screen.CoordinatorModule

/**
 * @author marian on 6.9.2017.
 */
@Module
abstract class ActivityFactoriesModule {

  @Module(includes = arrayOf(
//      ScreenModule::class,
      CoordinatorModule::class,
      FragmentFactoriesModule::class
  ))
  abstract class ActivityModules

  @ActivityScope @ContributesAndroidInjector(modules = arrayOf(ActivityModules::class))
  abstract fun contributeMainActivity(): MainActivity
}
