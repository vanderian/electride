package sk.vander.electride.ui.routes.detail

import com.f2prateek.rx.preferences2.Preference
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.mergeAllSingles
import io.reactivex.rxkotlin.zipWith
import sk.vander.electride.R
import sk.vander.electride.data.OffRoutePref
import sk.vander.electride.data.RangePref
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RouteStatsDao
import sk.vander.electride.net.api.OpenChargeApiService
import sk.vander.electride.net.model.ChargePoint
import sk.vander.electride.ui.DetailIntents
import sk.vander.electride.ui.DetailState
import sk.vander.electride.ui.polyline
import sk.vander.electride.ui.recharges
import sk.vander.lib.debug.log
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailModel @Inject constructor(
    private val routeDao: RouteDao,
    private val routeStatsDao: RouteStatsDao,
    @RangePref private val range: Preference<String>,
    @OffRoutePref private val offRoute: Preference<String>,
    private val service: OpenChargeApiService
) : ScreenModel<DetailState, DetailIntents>(DetailState()) {

  override fun collectIntents(intents: DetailIntents, result: Observable<Result>): Disposable =
      Observable.merge(
          intents.args().flatMapObservable {
            routeDao.queryOne(it).zipWith(routeStatsDao.queryRoute(it), { r, rs -> r.to(rs) })
                .map {
                  state.value.copy(polyline = it.second.single().geometry.polyline(), route = it.first,
                      stats = it.second.single(), recharges = it.second.single().distance.recharges(range.get().toInt()))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { state.onNext(it) }
                .toObservable()
          },
          intents.navigation().doOnNext { state.next { copy(view = it.itemId) } }
              .filter { it.itemId == R.id.view_mapbox }
              .filter { state.value.markers.isEmpty() }
              .flatMap { chargePois() },
          intents.mapReady().doOnSuccess { state.next { copy(mapLoading = false) } }.toObservable()
      )
          .doOnDispose { state.next { copy(mapLoading = true) } }
          .subscribe()

  private fun chargePois(): Observable<List<MarkerOptions>> = Observable.just(state.value.polyline!!.points)
      .map { it.filter(offRoute.get().toInt() * 1000) }
      .map { it.map { service.fetchPoi(it.latitude, it.longitude, offRoute.get().toInt()) } }
      .flatMapSingle {
        Observable.fromIterable(it)
            .mergeAllSingles()
            .collectInto(mutableListOf<ChargePoint>(), { acc, list -> acc.addAll(list) })
            .map { it.toSet() }
      }
      .log("has pois")
      .map { it.map { it.marker() } }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext { state.next { copy(markers = it) } }

  private fun List<LatLng>.filter(distance: Int) =
      this.foldRight(listOf(first()), { point, acc -> if (point.distanceTo(acc.last()) >= distance) acc.plus(point) else acc })

  private fun ChargePoint.marker(): MarkerOptions = MarkerOptions()
      .position(LatLng(addressInfo.latitude, addressInfo.longitude))
      .title(addressInfo.title)
      .snippet("${addressInfo.address}, ${addressInfo.town}")
}