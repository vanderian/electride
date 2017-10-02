package sk.vander.electride.ui.common

import android.os.Bundle
import android.view.View
import butterknife.BindView
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Single
import sk.vander.electride.R
import sk.vander.lib.ui.screen.Screen
import sk.vander.lib.ui.screen.ScreenModel
import kotlin.reflect.KClass

/**
 * @author marian on 20.9.2017.
 */
abstract class MapBoxScreen<T : ScreenModel<U, V>, U : Screen.State, V : Screen.Intents>
(clazz: KClass<T>) : Screen<T, U, V>(clazz) {

  @BindView(R.id.view_mapbox) lateinit var map: MapView
  val mapBox: Single<MapboxMap> =
      Single.create<MapboxMap> { emitter -> map.getMapAsync { emitter.onSuccess(it) } }
          .cache()

  override fun layout(): Int = R.layout.screen_map

  override fun onResume() {
    super.onResume()
    map.onResume()
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