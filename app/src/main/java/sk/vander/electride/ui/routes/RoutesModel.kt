package sk.vander.electride.ui.routes

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.ui.RouteIntents
import sk.vander.electride.ui.routes.adapter.RouteItem
import sk.vander.electride.ui.routes.detail.RouteDetailScreen
import sk.vander.electride.ui.routes.directions.DirectionsScreen
import sk.vander.lib.ui.screen.ListState
import sk.vander.lib.ui.screen.NextScreen
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

/**
 * @author marian on 21.9.2017.
 */
class RoutesModel @Inject constructor(
    private val routeDao: RouteDao
) : ScreenModel<ListState<RouteItem>, RouteIntents>(ListState()) {

  override fun collectIntents(intents: RouteIntents, result: Observable<Result>): Disposable =
      Observable.merge(
          Observable.merge(
              intents.newRoute().map { NextScreen(DirectionsScreen()) },
              intents.routeSelected().map { NextScreen(RouteDetailScreen.newInstance(it.id)) }
          ).doOnNext { navigation.onNext(it) },
          database().toObservable())
          .subscribe()

  fun database(): Flowable<ListState<RouteItem>> = routeDao.queryAllWithStats()
      .map { it.map { RouteItem(it) } }
      .map { ListState(it, false, it.isEmpty()) }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext { state.onNext(it) }
}
