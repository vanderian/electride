package sk.vander.electride.ui.routes.detail

import com.mapbox.mapboxsdk.annotations.PolylineOptions
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
    intents.args().flatMapPublisher { mapPoints(it) }
        .doOnNext { state.next { copy(polyline = it) } }
        .subscribe()

  internal fun mapPoints(routeId: Long): Flowable<PolylineOptions> =
      routeStatsDao.queryRoute(routeId)
          .map { it.single().geometry.polyline() }
          .observeOn(AndroidSchedulers.mainThread())
}