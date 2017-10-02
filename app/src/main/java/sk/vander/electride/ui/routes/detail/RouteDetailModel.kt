package sk.vander.electride.ui.routes.detail

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RouteStatsDao
import sk.vander.electride.ui.DetailIntents
import sk.vander.electride.ui.DetailState
import sk.vander.electride.ui.polyline
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailModel @Inject constructor(
    private val routeDao: RouteDao,
    private val routeStatsDao: RouteStatsDao
) : ScreenModel<DetailState, DetailIntents>(DetailState()) {

  override fun collectIntents(intents: DetailIntents, result: Observable<Result>): Disposable =
      Observable.merge(
          intents.args().flatMapObservable {
            routeDao.queryOne(it).zipWith(routeStatsDao.queryRoute(it), { r, rs -> r.to(rs) })
                .map {
                  state.value.copy(polyline = it.second.single().geometry.polyline(),
                      route = it.first.toString(), stats = it.second.single().text())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { state.onNext(it) }
                .toObservable()
          },
          intents.navigation().doOnNext { state.next { copy(view = it.itemId) } }
      )
          .subscribe()

}