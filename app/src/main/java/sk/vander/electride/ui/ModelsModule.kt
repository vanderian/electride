package sk.vander.electride.ui

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.vander.electride.ui.report.SummaryModel
import sk.vander.electride.ui.report.page.SummaryPageModel
import sk.vander.electride.ui.routes.RoutesModel
import sk.vander.electride.ui.routes.detail.RouteDetailModel
import sk.vander.electride.ui.routes.directions.DirectionsModel
import sk.vander.lib.annotations.ViewModelKey

/**
 * @author marian on 21.9.2017.
 */
@Module
abstract class ModelsModule {

  @Binds @IntoMap @ViewModelKey(RoutesModel::class)
  abstract fun provideRoutesModel(model: RoutesModel): ViewModel

  @Binds @IntoMap @ViewModelKey(RouteDetailModel::class)
  abstract fun provideRouteDetailModel(model: RouteDetailModel): ViewModel

  @Binds @IntoMap @ViewModelKey(DirectionsModel::class)
  abstract fun provideDirectionsModel(model: DirectionsModel): ViewModel

  @Binds @IntoMap @ViewModelKey(SummaryModel::class)
  abstract fun provideSummaryModel(model: SummaryModel): ViewModel

  @Binds @IntoMap @ViewModelKey(SummaryPageModel::class)
  abstract fun provideSummaryPageModel(model: SummaryPageModel): ViewModel

}