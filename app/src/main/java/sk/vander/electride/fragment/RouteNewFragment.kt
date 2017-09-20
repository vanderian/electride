package sk.vander.electride.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.widget.text
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Single
import sk.vander.electride.R
import sk.vander.lib.ui.BaseFragment
import javax.inject.Inject

/**
 * @author marian on 20.9.2017.
 */
class RouteNewFragment : BaseFragment() {
  @BindView(R.id.text_location) lateinit var location: TextView
  @BindView(R.id.map) lateinit var map: MapView
  @Inject lateinit var viewModel: RouteNewViewModel

  override fun layout(): Int = R.layout.screen_route_new

  override fun onResume() {
    super.onResume()
    map.onResume()
    disposable.addAll(
        Single.create<MapboxMap> { emitter -> map.getMapAsync { emitter.onSuccess(it) } }
            .flatMapPublisher { map ->
              viewModel.location().doOnNext {
                map.cameraPosition = CameraPosition.Builder().zoom(15.0).target(LatLng(it)).build()
              }
            }
            .map { it.toString() }
            .subscribe(location.text()))
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    map.onCreate(savedInstanceState)
  }

  override fun onPause() {
    super.onPause()
    map.onPause()
  }

  override fun onStart() {
    super.onStart()
    map.onStart()
  }

  override fun onStop() {
    super.onStop()
    map.onStop()
  }

  override fun onDestroy() {
    super.onDestroy()
    map.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    map.onLowMemory()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    map.onSaveInstanceState(outState)
  }
}