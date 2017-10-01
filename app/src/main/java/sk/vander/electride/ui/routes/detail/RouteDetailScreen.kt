package sk.vander.electride.ui.routes.detail

import android.os.Bundle
import io.reactivex.Single
import sk.vander.electride.R
import sk.vander.electride.ui.*
import sk.vander.electride.ui.common.MapBoxScreen
import sk.vander.lib.ui.screen.Screen

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailScreen : MapBoxScreen<RouteDetailModel, DetailState, DetailIntents>(RouteDetailModel::class) {
  override fun layout(): Int = R.layout.screen_route_detail

  override fun intents(): DetailIntents = object : DetailIntents {
    override fun args(): Single<Long> = Single.just(arguments.getLong(ARG_ROUTE_ID))
  }

  override fun render(state: DetailState) {
    state.polyline?.let {
      mapBox.blockingGet().apply {
        newLine(context, it)
        animateCamera(it.camera(UiConst.CAMERA_PADDING), UiConst.CAMERA_UPDATE)
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