package sk.vander.electride.ui

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.MenuItem
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import sk.vander.electride.R
import sk.vander.electride.db.entity.Route
import sk.vander.electride.db.entity.RouteStats
import sk.vander.electride.ui.report.model.RangeReport
import sk.vander.electride.ui.report.model.RouteReport
import sk.vander.lib.ui.screen.Screen

/**
 * @author marian on 29.9.2017.
 */

//route
interface RouteIntents : Screen.Intents {
  fun newRoute(): Observable<Unit>
  fun routeSelected(): Observable<Route>
  fun menu(): Observable<MenuItem>
}

//directions
data class DirectionState(
    val mapLoading: Boolean = true,
    val fab: Boolean = false,
    val camera: CameraUpdate? = null,
    val points: List<LatLng> = emptyList(),
    val response: DirectionsResponse? = null,
    val polyline: PolylineOptions? = null,
    val toolbarMode: NavMode = NavMode.BACK,
    val saveVisible: Boolean = false,
    val loading: Boolean = false,
    val date: LocalDate? = null,
    val recurrence: Recurrence? = null,
    val info: String = "",
    val chargeMarkers: List<MarkerOptions> = emptyList(),
    val helpMarkers: List<LatLng> = emptyList()
) : Screen.State

interface DirectionIntents : Screen.Intents {
  fun compute(): Observable<Unit>
  fun mapReady(): Single<MapboxMap>
  fun date(): Observable<LocalDate>
  fun recurrence(): Observable<Recurrence>
  fun toolbarNav(): Observable<Unit>
  fun toolbarMenu(): Observable<MenuItem>
}

enum class NavMode(@DrawableRes val drawableRes: Int) {
  BACK(R.drawable.avd_back_close),
  CLEAR(R.drawable.avd_close_back)
}

enum class Recurrence(@StringRes val string: Int, val unit: ChronoUnit) {
  NONE(R.string.recurrence_none, ChronoUnit.FOREVER),
  DAILY(R.string.recurrence_daily, ChronoUnit.DAYS),
  WEEKDAYS(R.string.recurrence_weekdays, ChronoUnit.DAYS),
  WEEKLY(R.string.recurrence_weekly, ChronoUnit.WEEKS),
  MONTHLY(R.string.recurrence_monthly, ChronoUnit.MONTHS),
  YEARLY(R.string.recurrence_yearly, ChronoUnit.YEARS);

  companion object {
    fun from(@IdRes itemId: Int) = when (itemId) {
      R.id.action_none -> NONE
      R.id.action_daily -> DAILY
      R.id.action_wekdays -> WEEKDAYS
      R.id.action_weekly -> WEEKLY
      R.id.action_monthly -> MONTHLY
      R.id.action_yearly -> YEARLY
      else -> NONE
    }
  }
}

//detail
data class DetailState(
    val mapLoading: Boolean = true,
    val view: Int = R.id.view_info,
    val polyline: PolylineOptions? = null,
    val route: Route? = null,
    val stats: RouteStats? = null,
    val recharges: Int = -1,
    val chargeMarkers: List<MarkerOptions> = emptyList(),
    val helpMarkers: List<LatLng> = emptyList()
) : Screen.State

interface DetailIntents : Screen.Intents {
  fun args(): Single<Long>
  fun navigation(): Observable<MenuItem>
  fun mapReady(): Single<Unit>
}

//summary
data class SummaryState(
    val date: LocalDate = LocalDate.now()
) : Screen.State

interface SummaryIntents : Screen.Intents {
  fun toolbarNav(): Observable<Unit>
  fun toolbarMenu(): Observable<MenuItem>
}

data class SummaryPageState(
    val items: List<RouteReport> = emptyList(),
    val report: RangeReport? = null
) : Screen.State

interface SummaryPageIntents : Screen.Intents {
  fun args(): Single<Pair<LocalDate, LocalDate>>
}