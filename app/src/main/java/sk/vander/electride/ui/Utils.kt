package sk.vander.electride.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.mapbox.mapboxsdk.annotations.*
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import com.mapbox.services.commons.models.Position
import sk.vander.electride.R

/**
 * @author marian on 23.9.2017.
 */
fun DirectionsResponse.text() =
    "Route[" +
        "distance=${routes.single().distance}," +
        "duration=${routes.single().duration}," +
        waypoints.map { "Waypoint[${it.name} ${it.asPosition().latLng()}]" }.toString() +
        "]"

fun MapboxMap.newLineAndCamera(context: Context, polylineOptions: PolylineOptions) {
  removeAnnotations()
  polylineOptions.points.dropLast(1).drop(1)
      .forEach { addMarker(it.point(context, R.drawable.shape_dot, R.color.colorPrimary)) }
  addMarker(polylineOptions.points.first().point(context, R.drawable.ic_arrow_downward_black_24dp, android.R.color.white))
  addMarker(polylineOptions.points.last().point(context, R.drawable.ic_clear_black_24dp, android.R.color.white))
  animateCamera(addPolyline(polylineOptions).camera(100), 3000)
}

fun Position.latLng(): LatLng = LatLng(this.latitude, this.longitude, this.altitude)

fun LatLng.position(): Position = Position.fromCoordinates(this.longitude, this.latitude, this.altitude)

fun Location.camera(zoom: Double): CameraUpdate =
    CameraUpdateFactory.newLatLngZoom(LatLng(this.latitude, this.longitude), zoom)

fun Polyline.camera(padding: Int): CameraUpdate =
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
