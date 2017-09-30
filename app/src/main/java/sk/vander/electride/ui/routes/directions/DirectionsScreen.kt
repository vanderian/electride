package sk.vander.electride.ui.routes.directions

import android.support.design.widget.BottomSheetBehavior
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
  @BindView(R.id.text_sheet_title) lateinit var title: TextView
  @BindView(R.id.text_sheet_info) lateinit var info: TextView
  @BindView(R.id.fab_direction) lateinit var fab: FloatingActionButton
  @BindView(R.id.view_progress) lateinit var progress: View
  @BindView(R.id.bottom_sheet) lateinit var sheet: View

  override fun layout(): Int = R.layout.screen_directions

  override fun intents(): DirectionIntents = object : DirectionIntents {
    override fun compute(): Observable<Unit> = fab.clicks()
    override fun mapReady(): Single<MapboxMap> = mapBox
  }

  override fun render(state: DirectionState) {
    progress.visibility = state.loading.visibility()
    fab.visibility = state.fab.visibility()
//    if (state.fab) fab.show() else fab.hide()
    if (!state.loading) {
      val map = mapBox.blockingGet()
      state.camera?.let { map.animateCamera(it) }
      state.points.map { it.point(context, R.drawable.shape_dot, R.color.amber_600) }
          .let { if (it.isNotEmpty()) map.addMarker(it.last()) }
      title.text = if (state.points.isEmpty()) getString(R.string.no_points) else getString(R.string.has_points, state.points.size)
      info.text = state.response?.text() ?: map.markers.map { it.position }.joinToString("\n\n")
      state.polyline?.let { map.newLineAndCamera(context, it) }
      state.response?.let { BottomSheetBehavior.from(sheet).state = BottomSheetBehavior.STATE_EXPANDED }
    }
  }
}