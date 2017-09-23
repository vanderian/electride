package sk.vander.electride.ui.routes.directions

import android.arch.lifecycle.ViewModel
import android.graphics.Color
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.services.Constants
import com.mapbox.services.api.directions.v5.DirectionsCriteria
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import com.mapbox.services.api.rx.directions.v5.MapboxDirectionsRx
import com.mapbox.services.commons.geojson.LineString
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import sk.vander.electride.BuildConfig
import sk.vander.electride.ui.latLng
import sk.vander.electride.ui.position
import javax.inject.Inject

/**
 * @author marian on 23.9.2017.
 */
class DirectionsViewModel @Inject constructor() : ViewModel() {

  fun directions(points: List<LatLng>): Observable<DirectionsResponse> =
      MapboxDirectionsRx.Builder()
          .setGeometry(DirectionsCriteria.GEOMETRY_POLYLINE6)
          .setProfile(DirectionsCriteria.PROFILE_DRIVING)
          .setAccessToken(BuildConfig.MAPBOX_TOKEN)
          .setOrigin(points.first().position())
          .setDestination(points.last().position())
          .build().apply { isEnableDebug = true }
          .observable
          .subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())

  fun polyline(): Function <DirectionsResponse, PolylineOptions> = Function {
    LineString.fromPolyline(it.routes.single().geometry, Constants.PRECISION_6)
        .coordinates.map { it.latLng() }
        .let { PolylineOptions().addAll(it).width(5f).alpha(0.5f).color(Color.WHITE) }
  }

}