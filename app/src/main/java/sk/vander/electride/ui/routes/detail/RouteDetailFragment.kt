package sk.vander.electride.ui.routes.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import sk.vander.electride.R
import sk.vander.electride.ui.common.MapBoxFragment
import sk.vander.electride.ui.newLineAndCamera
import sk.vander.lib.debug.log

/**
 * @author marian on 22.9.2017.
 */
class RouteDetailFragment : MapBoxFragment<RouteDetailViewModel>(RouteDetailViewModel::class) {

  override fun layout(): Int = R.layout.screen_route_detail

  override fun onStart() {
    super.onStart()
    val id = arguments.getLong(ARG_ROUTE_ID)
    disposable.addAll(
        mapBox.flatMapPublisher { map ->
          viewModel.mapPoints(id)
              .log("has points")
              .observeOn(AndroidSchedulers.mainThread())
              .doOnNext { map.newLineAndCamera(context, it) }
        }
            .subscribe()
    )
  }

  companion object {
    const val ARG_ROUTE_ID = "arg_route_id"

    @JvmStatic fun newInstance(routeId: Long): Fragment = RouteDetailFragment().apply {
      arguments = Bundle().apply { putLong(ARG_ROUTE_ID, routeId) }
    }
  }
}