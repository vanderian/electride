package sk.vander.electride.ui.routes.directions

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.api.directions.v5.DirectionsCriteria
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import com.mapbox.services.api.rx.directions.v5.MapboxDirectionsRx
import com.mapbox.services.commons.models.Position
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import sk.vander.electride.BuildConfig
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RouteStatsDao
import sk.vander.electride.db.entity.Route
import sk.vander.electride.db.entity.RouteStats
import sk.vander.electride.ui.*
import sk.vander.lib.debug.log
import sk.vander.lib.ui.screen.GoBack
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

/**
 * @author marian on 23.9.2017.
 */
class DirectionsModel @Inject constructor(
    private val routeDao: RouteDao,
    private val routeStatsDao: RouteStatsDao
) : ScreenModel<DirectionState, DirectionIntents>(DirectionState()) {

  override fun collectIntents(intents: DirectionIntents, result: Observable<Result>): Disposable {
    val disposable = CompositeDisposable()
    val boundary = PublishSubject.create<Unit>()
    disposable.addAll(
        intents.compute().subscribe { boundary.onNext(Unit) },
        intents.mapReady()
            .doOnSuccess { it.isMyLocationEnabled = true }
            .doOnSuccess { state.next { copy(mapLoading = false, camera = it.myLocation?.camera(10.0)) } }
            .flatMapObservable { map ->
              map.mapClicks()
                  .filter { state.value.loading.not().or(state.value.response == null) }
                  .doOnNext { state.next { copy(points = points.plus(it), fab = points.isNotEmpty()) } }
                  .doAfterNext { if (map.markers.size >= API_LIMIT) boundary.onNext(Unit) }
                  .buffer(boundary)
                  .doOnNext { state.next { copy(fab = false, loading = true) } }
                  .map { it.map { it.position() } }
                  .flatMapSingle { directions(it) }
                  .log("has direction")
                  .doOnNext {
                    state.next {
                      copy(response = it, polyline = it.routes.single().geometry.polyline(),
                          camera = polyline?.camera(UiConst.CAMERA_PADDING), toolbarMode = NavMode.CLEAR, saveVisible = true)
                    }
                  }
            }
            .doOnDispose { state.next { copy(mapLoading = true) } }
            .subscribe(),
        intents.date().subscribe { state.next { copy(date = it) } },
        intents.recurrence().subscribe { state.next { copy(recurrence = it) } },
        intents.toolbarNav().map { state.value.toolbarMode }.subscribe {
          if (it == NavMode.BACK) navigation.onNext(GoBack) else state.onNext(DirectionState(mapLoading = false))
        },
        intents.toolbarMenu()
            .map { Route(date = state.value.date!!, recurrence = state.value.recurrence!!) }
            .flatMapSingle {
              Single.fromCallable { routeDao.insert(it) }
                  .subscribeOn(Schedulers.io())
                  .map { RouteStats.create(it, state.value.response!!) }
                  .flatMap { Single.fromCallable { routeStatsDao.insert(it) } }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .log("inserted")
            .doOnNext { navigation.onNext(GoBack) }
            .subscribe()
    )
    return disposable
  }

  private fun MapboxMap.mapClicks() = Observable.create<LatLng> { emitter ->
    this.setOnMapClickListener { emitter.onNext(it) }
    emitter.setCancellable { this.setOnMapClickListener(null) }
  }

  private fun directions(points: List<Position>): Single<DirectionsResponse> =
      MapboxDirectionsRx.Builder()
          .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE6)
          .setProfile(DirectionsCriteria.PROFILE_DRIVING)
          .setAccessToken(BuildConfig.MAPBOX_TOKEN)
          .setCoordinates(points)
          .build().apply { isEnableDebug = true }
          .observable.firstOrError()
          .subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())

  companion object {
    const val API_LIMIT = 25 // mapbox directions api limit
  }
}