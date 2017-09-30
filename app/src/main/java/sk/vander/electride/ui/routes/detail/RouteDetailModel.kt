package sk.vander.electride.ui.routes.detail

import android.graphics.Color
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import sk.vander.electride.db.dao.RoutePointDao
import sk.vander.electride.ui.DetailIntents
import sk.vander.electride.ui.DetailState
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailModel @Inject constructor(
    private val routePointDao: RoutePointDao
) : ScreenModel<DetailState, DetailIntents>(DetailState()) {

  override fun collectIntents(intents: DetailIntents, result: Observable<Result>): Disposable =
    intents.args().flatMapPublisher { mapPoints(it) }
        .doOnNext { state.next { copy(polyline = it) } }
        .subscribe()

  internal fun mapPoints(routeId: Long): Flowable<PolylineOptions> =
      routePointDao.queryRoute(routeId)
          .map { it.filter { it.accuracy < 25f } }
          .map { it.map { LatLng(it.latitude, it.longitude) } }
          .map { PolylineOptions().addAll(it).width(5f).alpha(0.5f).color(Color.WHITE) }
}