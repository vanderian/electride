package sk.vander.electride.ui

import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import io.reactivex.Observable
import io.reactivex.Single
import sk.vander.electride.db.entity.Route
import sk.vander.lib.ui.screen.Screen

/**
 * @author marian on 29.9.2017.
 */

//route
interface RouteIntents: Screen.Intents {
  fun newRoute(): Observable<Unit>
  fun routeSelected(): Observable<Route>
}

//directions
data class DirectionState(
    val loading: Boolean = true,
    val fab: Boolean = false,
    val camera: CameraUpdate? = null,
    val points: List<LatLng> = emptyList(),
    val response: DirectionsResponse? = null,
    val polyline: PolylineOptions? = null
): Screen.State

interface DirectionIntents: Screen.Intents {
  fun compute(): Observable<Unit>
  fun mapReady(): Single<MapboxMap>
}

//detail
data class DetailState(
    val polyline: PolylineOptions? = null
): Screen.State
interface DetailIntents: Screen.Intents {
  fun args(): Single<Long>
}