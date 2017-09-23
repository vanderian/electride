package sk.vander.electride.ui

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.vander.electride.ui.routes.directions.DirectionsViewModel
import sk.vander.electride.ui.routes.detail.RouteDetailViewModel
import sk.vander.electride.ui.routes.RoutesViewModel
import sk.vander.lib.annotations.ViewModelKey

/**
 * @author marian on 21.9.2017.
 */
@Module
abstract class ViewModelsModule {

  @Binds @IntoMap @ViewModelKey(RoutesViewModel::class)
  abstract fun provideRoutesViewModel(viewModel: RoutesViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(RouteDetailViewModel::class)
  abstract fun provideRouteDetailViewModel(viewModel: RouteDetailViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(DirectionsViewModel::class)
  abstract fun providePickLocationViewModel(viewModel: DirectionsViewModel): ViewModel

}