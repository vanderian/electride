package sk.vander.electride.ui.routes.directions

import android.graphics.Color
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.Constants
import com.mapbox.services.api.directions.v5.DirectionsCriteria
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import com.mapbox.services.api.rx.directions.v5.MapboxDirectionsRx
import com.mapbox.services.commons.geojson.LineString
import com.mapbox.services.commons.models.Position
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import sk.vander.electride.BuildConfig
import sk.vander.electride.ui.*
import sk.vander.lib.debug.log
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

/**
 * @author marian on 23.9.2017.
 */
class DirectionsModel @Inject constructor() : ScreenModel<DirectionState, DirectionIntents>(DirectionState()) {

  override fun collectIntents(intents: DirectionIntents, result: Observable<Result>): Disposable =
      intents.mapReady()
          .doOnSuccess { it.isMyLocationEnabled = true }
          .doOnSuccess { state.next { copy(loading = false, camera = it.myLocation?.camera(10.0)) } }
          .flatMap { map ->
            map.mapClicks()
                .doOnDispose { state.next { copy(fab = false) } }
                .doOnNext { state.next { copy(points = points.plus(it), fab = points.isNotEmpty()) } }
                .takeUntil { map.markers.size >= API_LIMIT }
                .takeUntil(intents.compute()).toList()
                .map { it.map { it.position() } }
                .flatMap { directions(it) }
                .log("has direction")
                .doOnSuccess { state.next { copy(response = it, polyline = it.polyline()) } }
          }
          .doOnDispose { state.next { copy(loading = true) } }
          .subscribe()

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

  private fun DirectionsResponse.polyline(): PolylineOptions =
      LineString.fromPolyline(routes.single().geometry, Constants.PRECISION_6)
          .coordinates.map { it.latLng() }
          .let { PolylineOptions().addAll(it).width(WIDTH).alpha(ALPHA).color(Color.WHITE) }

  companion object {
    const val API_LIMIT = 25 // mapbox directions api limit
    const val WIDTH = 5f
    const val ALPHA = 0.5f
  }
}