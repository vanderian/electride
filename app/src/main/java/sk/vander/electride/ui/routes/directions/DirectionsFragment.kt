package sk.vander.electride.ui.routes.directions

import android.support.design.widget.FloatingActionButton
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import sk.vander.electride.R
import sk.vander.electride.ui.camera
import sk.vander.electride.ui.common.MapBoxFragment
import sk.vander.electride.ui.latLng
import sk.vander.electride.ui.newLineAndCamera
import sk.vander.electride.ui.point
import sk.vander.lib.debug.log

/**
 * @author marian on 23.9.2017.
 */
class DirectionsFragment : MapBoxFragment<DirectionsViewModel>(DirectionsViewModel::class) {
  @BindView(R.id.text_points) lateinit var points: TextView
  @BindView(R.id.fab_direction) lateinit var fab: FloatingActionButton

  override fun layout(): Int = R.layout.screen_directions

  override fun onStart() {
    super.onStart()

    disposable.addAll(
        mapBox
            .doOnSuccess {
              it.isMyLocationEnabled = true
              it.animateCamera(it.myLocation?.camera(10.0))
            }
            .flatMapObservable { map ->
              map.mapClicks()
                  .doOnNext { if (map.markers.size > 1) fab.show() }
                  .doOnDispose { fab.hide() }
                  .doOnNext { map.addMarker(it.point(context, R.drawable.shape_dot, R.color.amber_600)) }
                  .doOnNext { points.text = map.markers.toString() }
                  .takeUntil(fab.clicks()).toList()
                  .flatMapObservable { viewModel.directions(it) }
                  .log("has direction")
                  .doOnNext { points.text = it.waypoints.map { "Waypoint[${it.name} ${it.asPosition().latLng()}]" }.toString() }
                  .map(viewModel.polyline())
                  .doOnNext { map.newLineAndCamera(context, it) }
            }
            .subscribe()
    )
  }

  private fun MapboxMap.mapClicks() = Observable.create<LatLng> { emitter ->
    this.setOnMapClickListener { emitter.onNext(it) }
    emitter.setCancellable { this.setOnMapClickListener(null) }
  }


}