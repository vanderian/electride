package sk.vander.electride.ui

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.vander.electride.ui.create.RouteNewViewModel
import sk.vander.electride.ui.routes.RoutesViewModel
import sk.vander.lib.annotations.ViewModelKey

/**
 * @author marian on 21.9.2017.
 */
@Module
abstract class ViewModelsModule {

  @Binds @IntoMap @ViewModelKey(RoutesViewModel::class)
  abstract fun provideRoutesViewModel(routesViewModel: RoutesViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(RouteNewViewModel::class)
  abstract fun provideRouteNewViewModel(routeNewViewModel: RouteNewViewModel): ViewModel

}