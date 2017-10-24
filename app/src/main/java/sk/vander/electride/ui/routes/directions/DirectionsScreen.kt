package sk.vander.electride.ui.routes.directions

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.focusChanges
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import sk.vander.electride.R
import sk.vander.electride.ui.*
import sk.vander.electride.ui.common.MapBoxScreen

/**
 * @author marian on 23.9.2017.
 */
class DirectionsScreen : MapBoxScreen<DirectionsModel, DirectionState, DirectionIntents>(DirectionsModel::class) {
  private val dialog: Maybe<LocalDate> by lazy {
    Maybe.create<LocalDate> { emitter ->
      val date = LocalDate.now()
      val d = DatePickerDialog(context,
          DatePickerDialog.OnDateSetListener { _, y, m, d -> emitter.onSuccess(LocalDate.of(y, m + 1, d)) },
          date.year, date.monthValue - 1, date.dayOfMonth)
      d.setOnDismissListener { emitter.onComplete() }
      emitter.setCancellable { d.dismiss() }
      d.show()
    }
  }

  private val popup: Maybe<Recurrence> by lazy {
    Maybe.create<Recurrence> { emitter ->
      val popup = PopupMenu(context, editRecurrence).apply {
        menuInflater.inflate(R.menu.menu_popup_recurrence, menu)
        setOnMenuItemClickListener { emitter.onSuccess(Recurrence.from(it.itemId)); true }
        setOnDismissListener { emitter.onComplete() }
      }
      emitter.setCancellable { popup.dismiss() }
      popup.show()
    }
  }

  @BindView(R.id.text_sheet_title) lateinit var title: TextView
  @BindView(R.id.text_sheet_info) lateinit var info: TextView
  @BindView(R.id.fab_direction) lateinit var fab: FloatingActionButton
  @BindView(R.id.view_progress) lateinit var progress: View
  @BindView(R.id.bottom_sheet) lateinit var sheet: View
  @BindView(R.id.input_edit_date) lateinit var editDate: TextInputEditText
  @BindView(R.id.input_edit_recurrence) lateinit var editRecurrence: TextInputEditText
  @BindView(R.id.input_date) lateinit var inputDate: TextInputLayout
  @BindView(R.id.input_recurrence) lateinit var inputRecurrence: TextInputLayout
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

  override fun layout(): Int = R.layout.screen_directions

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    toolbar.inflateMenu(R.menu.menu_directions)
  }

  override fun intents(): DirectionIntents = object : DirectionIntents {

    override fun date(): Observable<LocalDate> =
        editDate.focusChanges().filter { it }.doOnNext { editDate.clearFocus() }
            .flatMapMaybe { dialog }
            .startWith(LocalDate.now())
            .doOnNext { editDate.setText(it.format(DateTimeFormatter.ISO_LOCAL_DATE)) }

    override fun recurrence(): Observable<Recurrence> =
        editRecurrence.focusChanges().filter { it }.doOnNext { editRecurrence.clearFocus() }
            .flatMapMaybe { popup }
            .startWith(Recurrence.NONE)
            .doOnNext { editRecurrence.setText(it.string) }

    override fun compute(): Observable<Unit> = fab.clicks()
    override fun mapReady(): Single<MapboxMap> = mapBox
    override fun toolbarMenu(): Observable<MenuItem> = toolbar.itemClicks()
    override fun toolbarNav(): Observable<Unit> = toolbar.navigationClicks()
  }

  override fun render(state: DirectionState) {
    progress.visibility = state.mapLoading.visibility()
    fab.visibility = state.fab.visibility()
    inputDate.visibility = (state.response != null).visibility()
    inputRecurrence.visibility = (state.response != null).visibility()
    toolbar.menu.findItem(R.id.action_save).toggle(state.saveVisible)
    toolbar.toggle(state.toolbarMode)
//    if (state.fab) fab.show() else fab.hide()
    if (!state.mapLoading) {
      val map = mapBox.blockingGet()
      if (state.points.isEmpty() && state.polyline == null) {
        map.removeAnnotations()
        sheet.toggle(false)
      }
      state.camera?.let {
        map.animateCamera(it, UiConst.CAMERA_UPDATE, cameraCallback {
          state.response?.let { sheet.toggle(true) }
        })
      }
      state.points.map { it.point(context, R.drawable.shape_dot, R.color.amber_600) }
          .let { if (it.isNotEmpty() && it.size != map.markers.size) map.addMarker(it.last()) }
      title.text = title(state)
      info.text = state.info
      state.polyline?.let {
        if (map.polylines.isEmpty()) {
          map.newLine(context, it)
          state.response!!.waypoints
              .map { it.asLatLng().point(context, R.drawable.ic_place_black_24dp, R.color.colorPrimary).title(it.name) }
              .drop(1).dropLast(1)
              .forEach { map.addMarker(it) }
        }
      }
      state.helpMarkers.forEach { map.addMarker(it.point(context, R.drawable.shape_dot, R.color.green_600)) }
      state.chargeMarkers.forEach { map.addMarker(it.icon(R.drawable.ic_ev_station_black_24dp.icon(context, android.R.color.white))) }
    }
  }

  private fun View.toggle(expand: Boolean) {
    BottomSheetBehavior.from(this).state =
        if (expand) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
  }

  private fun title(state: DirectionState): String =
      when {
        state.response != null -> getString(R.string.has_way_points, state.response.waypoints.size)
        state.points.isNotEmpty() -> getString(R.string.has_points, state.points.size)
        else -> getString(R.string.no_points)
      }
}
