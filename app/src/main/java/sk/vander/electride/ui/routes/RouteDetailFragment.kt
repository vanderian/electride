package sk.vander.electride.ui.routes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.mapbox.mapboxsdk.annotations.*
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.android.schedulers.AndroidSchedulers
import sk.vander.electride.R
import sk.vander.electride.ui.common.MapBoxFragment
import sk.vander.lib.debug.log

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailFragment : MapBoxFragment<RouteDetailViewModel>(RouteDetailViewModel::class.java) {

  override fun layout(): Int = R.layout.screen_route_detail

  override fun onStart() {
    super.onStart()
    val id = arguments.getLong(ARG_ROUTE_ID)
    disposable.addAll(
        mapBox.flatMapPublisher { map ->
          viewModel.mapPoints(id)
              .log("has points")
              .observeOn(AndroidSchedulers.mainThread())
              .doOnNext { map.newLineAndCamera(it) }
        }
            .subscribe()
    )
  }

  fun MapboxMap.newLineAndCamera(polylineOptions: PolylineOptions) {
    polylines.firstOrNull()?.let { removePolyline(it) }
    polylineOptions.points.dropLast(1).drop(1).forEach { addMarker(it.point(R.drawable.shape_dot, R.color.colorPrimary)) }
    addMarker(polylineOptions.points.first().point(R.drawable.ic_arrow_downward_black_24dp, android.R.color.white))
    addMarker(polylineOptions.points.last().point(R.drawable.ic_clear_black_24dp, android.R.color.white))
    animateCamera(addPolyline(polylineOptions).camera(), 3000)
  }

  fun Polyline.camera(): CameraUpdate =
      CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder().includes(this.points).build(), 100)

  fun LatLng.point(@DrawableRes drawable: Int, @ColorRes color: Int): MarkerOptions =
      MarkerOptions().position(this).icon(drawable.icon(color))

  fun Int.icon(@ColorRes color: Int): Icon = IconFactory.getInstance(context).fromBitmap(
      ContextCompat.getDrawable(context, this).toBitmap(color)
  )

  fun Drawable.toBitmap(@ColorRes color: Int): Bitmap =
      Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
        Canvas().let {
          DrawableCompat.setTint(this@toBitmap, ContextCompat.getColor(context, color))
          it.setBitmap(this)
          setBounds(0, 0, this@toBitmap.intrinsicWidth, this@toBitmap.intrinsicHeight)
          draw(it)
        }
      }

  companion object {
    const val ARG_ROUTE_ID = "arg_route_id"

    @JvmStatic fun newInstance(routeId: Long): Fragment = RouteDetailFragment().apply {
      arguments = Bundle().apply { putLong(ARG_ROUTE_ID, routeId) }
    }
  }
}