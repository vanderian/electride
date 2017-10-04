package sk.vander.electride.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.location.Location
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.Animatable2Compat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.Constants
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import com.mapbox.services.commons.geojson.LineString
import com.mapbox.services.commons.models.Position
import org.threeten.bp.Duration
import org.threeten.bp.temporal.ChronoUnit
import sk.vander.electride.R
import java.util.concurrent.TimeUnit

/**
 * @author marian on 23.9.2017.
 */

////////
// View

object UiConst {
  const val WIDTH = 5f
  const val ALPHA = 0.5f
  const val CAMERA_PADDING = 100
  const val CAMERA_UPDATE = 3000
}

fun MenuItem.toggle(visible: Boolean) {
  if (!isVisible && visible) icon.animate()
  isVisible = visible
}

fun Toolbar.toggle(mode: NavMode) {
  if (tag == null) {
    setNavigationIcon(mode.drawableRes)
  } else if (tag != mode) {
    navigationIcon?.animate { setNavigationIcon(mode.drawableRes) }
  }
  tag = mode
}

fun Drawable.animate(action: () -> Unit) {
  if (this is Animatable2Compat) {
    registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
      override fun onAnimationEnd(drawable: Drawable?) {
        action()
        clearAnimationCallbacks()
      }
    })
    start()
  }
}

fun Drawable.animate() {
  if (this is Animatable) start()
}

fun Boolean.visibility() = if (this) View.VISIBLE else View.GONE

////////
// Data

fun Double.recharges(range: Int) = (this / 1000).toInt() / range

fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)

fun Double.toKm() = this.div(1000).format(2) + " km"

fun Double.toDmhs() = Duration.ofSeconds(this.toLong()).format()

fun DirectionsResponse.text() =
    "Distance=${routes.single().distance.div(1000).format(2)} km, " +
        "Duration=${TimeUnit.SECONDS.toMinutes(routes.single().duration.toLong())} min,\n\n" +
        waypoints.map { "${it.name} - ${it.asPosition().latLng()}]" }.joinToString("\n\n")

fun Duration.format():String {
  val days = toDays()
  val hours = this.minusDays(days).toHours()
  val minutes = this.minusDays(days).minusHours(hours).toMinutes()
  val sec = this.minusDays(days).minusHours(hours).minusMinutes(minutes)[ChronoUnit.SECONDS]
  val sb = StringBuilder()
  if (days > 0) sb.append("${days}d ")
  if (hours > 0 || sb.isNotEmpty()) sb.append("${hours}h ")
  if (minutes > 0 || sb.isNotEmpty()) sb.append("${minutes}min ")
  if (sec > 0 || sb.isNotEmpty()) sb.append("${sec}s")
  return sb.toString()
}

/////////
// Mapbox

fun cameraCallback(finish: () -> Unit): MapboxMap.CancelableCallback = object : MapboxMap.CancelableCallback {
  override fun onFinish() {
    finish()
  }

  override fun onCancel() {
//    cancel()
  }
}

fun String.polyline(): PolylineOptions =
    LineString.fromPolyline(this, Constants.PRECISION_6)
        .coordinates.map { it.latLng() }
        .let { PolylineOptions().addAll(it).width(UiConst.WIDTH).alpha(UiConst.ALPHA).color(Color.WHITE) }


fun MapboxMap.newLine(context: Context, polylineOptions: PolylineOptions) {
  removeAnnotations()
//  polylineOptions.points.dropLast(1).drop(1)
//      .forEach { addMarker(it.point(context, R.drawable.shape_dot, R.color.colorPrimary)) }
  addMarker(polylineOptions.points.first().point(context, R.drawable.ic_arrow_downward_black_24dp, android.R.color.white))
  addMarker(polylineOptions.points.last().point(context, R.drawable.ic_clear_black_24dp, android.R.color.white))
  addPolyline(polylineOptions)
}

fun Position.latLng(): LatLng = LatLng(this.latitude, this.longitude, this.altitude)

fun LatLng.position(): Position = Position.fromCoordinates(this.longitude, this.latitude, this.altitude)

fun Location.camera(zoom: Double): CameraUpdate =
    CameraUpdateFactory.newLatLngZoom(LatLng(this.latitude, this.longitude), zoom)

fun PolylineOptions.camera(padding: Int): CameraUpdate =
    CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder().includes(this.points).build(), padding)

fun LatLng.point(context: Context, @DrawableRes drawable: Int, @ColorRes color: Int): MarkerOptions =
    MarkerOptions().position(this).icon(drawable.icon(context, color))

fun Int.icon(context: Context, @ColorRes color: Int): Icon = IconFactory.getInstance(context).fromBitmap(
    ContextCompat.getDrawable(context, this).toBitmap(context, color)
)

fun Drawable.toBitmap(context: Context, @ColorRes color: Int): Bitmap =
    Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
      Canvas().let {
        DrawableCompat.setTint(this@toBitmap, ContextCompat.getColor(context, color))
        it.setBitmap(this)
        setBounds(0, 0, this@toBitmap.intrinsicWidth, this@toBitmap.intrinsicHeight)
        draw(it)
      }
    }
