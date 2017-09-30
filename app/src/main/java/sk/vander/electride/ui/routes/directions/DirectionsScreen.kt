package sk.vander.electride.ui.routes.directions

import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import io.reactivex.Single
import sk.vander.electride.R
import sk.vander.electride.ui.*
import sk.vander.electride.ui.common.MapBoxScreen

/**
 * @author marian on 23.9.2017.
 */
class DirectionsScreen : MapBoxScreen<DirectionsModel, DirectionState, DirectionIntents>(DirectionsModel::class) {
  @BindView(R.id.text_info) lateinit var info: TextView
  @BindView(R.id.fab_direction) lateinit var fab: FloatingActionButton
  @BindView(R.id.view_progress) lateinit var progress: View

  override fun layout(): Int = R.layout.screen_directions

  override fun intents(): DirectionIntents = object : DirectionIntents {
    override fun compute(): Observable<Unit> = fab.clicks()
    override fun mapReady(): Single<MapboxMap> = mapBox
  }

  override fun render(state: DirectionState) {
    progress.visibility = state.loading.visibility()
    if (state.fab) fab.show() else fab.hide()
    if (!state.loading) {
      val map = mapBox.blockingGet()
      state.camera?.let { map.animateCamera(it) }
      state.points.map { it.point(context, R.drawable.shape_dot, R.color.amber_600) }
          .let { if (it.isNotEmpty()) map.addMarker(it.last()) }
      info.text = state.response?.text() ?: map.markers.toString()
      state.polyline?.let { map.newLineAndCamera(context, it) }
      state.response?.let { BottomSheetDialog(context).show() }
    }
  }
}