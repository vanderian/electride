package sk.vander.electride.ui.routes.detail

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.support.design.widget.itemSelections
import io.reactivex.Observable
import io.reactivex.Single
import sk.vander.electride.R
import sk.vander.electride.ui.*
import sk.vander.electride.ui.common.MapBoxScreen
import sk.vander.lib.ui.screen.Screen
import sk.vander.lib.ui.widget.BetterViewAnimator

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailScreen : MapBoxScreen<RouteDetailModel, DetailState, DetailIntents>(RouteDetailModel::class) {
  @BindView(R.id.animator) lateinit var viewAnimator: BetterViewAnimator
  @BindView(R.id.navigation) lateinit var viewNavigation: BottomNavigationView
  @BindView(R.id.text_route) lateinit var textRoute: TextView
  @BindView(R.id.text_route_stats) lateinit var textRouteStats: TextView
  @BindView(R.id.text_recharges) lateinit var textRecharges: TextView

  override fun layout(): Int = R.layout.screen_route_detail

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewNavigation.itemSelections()
  }

  override fun intents(): DetailIntents = object : DetailIntents {
    override fun mapReady(): Single<Unit> = mapBox.map { Unit }
    override fun navigation(): Observable<MenuItem> = viewNavigation.itemSelections()
    override fun args(): Single<Long> = Single.just(arguments.getLong(ARG_ROUTE_ID))
  }

  override fun render(state: DetailState) {
    viewAnimator.displayedChildId = state.view
    textRoute.text = state.route.toString()
    textRouteStats.text = state.stats.toString()
    textRecharges.text = when {
      state.recharges == 0 -> getString(R.string.no_recharges)
      state.recharges > 0 -> getString(R.string.some_recharges, state.recharges)
      else -> ""
    }
    if (state.mapLoading.not() && viewAnimator.displayedChildId == map.id) {
      mapBox.blockingGet().apply {
        if (polylines.isEmpty() && state.polyline != null) {
          newLine(context, state.polyline)
          animateCamera(state.polyline.camera(UiConst.CAMERA_PADDING), UiConst.CAMERA_UPDATE)
        }
        if (markers.size == 2 && state.chargeMarkers.isNotEmpty()) {
          state.helpMarkers.forEach { addMarker(it.point(context, R.drawable.shape_dot, R.color.green_600)) }
          state.chargeMarkers.forEach { addMarker(it.icon(R.drawable.ic_ev_station_black_24dp.icon(context, android.R.color.white))) }
        }
      }
    }
  }

  companion object {
    const val ARG_ROUTE_ID = "arg_route_id"

    @JvmStatic fun newInstance(routeId: Long): Screen<*, *, *> = RouteDetailScreen().apply {
      arguments = Bundle().apply { putLong(ARG_ROUTE_ID, routeId) }
    }
  }
}