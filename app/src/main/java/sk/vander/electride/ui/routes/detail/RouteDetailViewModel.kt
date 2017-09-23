package sk.vander.electride.ui.routes.detail

import android.arch.lifecycle.ViewModel
import android.graphics.Color
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.Flowable
import sk.vander.electride.db.dao.RoutePointDao
import javax.inject.Inject

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailViewModel @Inject constructor(
    private val routePointDao: RoutePointDao
) : ViewModel() {

  internal fun mapPoints(routeId: Long): Flowable<PolylineOptions> =
      routePointDao.queryRoute(routeId)
          .map { it.filter { it.accuracy < 25f }}
          .map { it.map { LatLng(it.latitude, it.longitude) } }
          .map { PolylineOptions().addAll(it).width(5f).alpha(0.5f).color(Color.WHITE) }
}