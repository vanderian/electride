package sk.vander.electride.ui

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.vander.electride.ui.routes.RoutesModel
import sk.vander.electride.ui.routes.directions.DirectionsModel
import sk.vander.lib.annotations.ViewModelKey

/**
 * @author marian on 21.9.2017.
 */
@Module
abstract class ModelsModule {

  @Binds @IntoMap @ViewModelKey(RoutesModel::class)
  abstract fun provideRoutesModel(model: RoutesModel): ViewModel

//  @Binds @IntoMap @ViewModelKey(RouteDetailViewModel::class)
//  abstract fun provideRouteDetailModel(model: RouteDetailViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(DirectionsModel::class)
  abstract fun provideDirectionsModel(model: DirectionsModel): ViewModel

}