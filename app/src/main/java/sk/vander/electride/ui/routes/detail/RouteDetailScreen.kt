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

  override fun layout(): Int = R.layout.screen_route_detail

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewNavigation.itemSelections()
  }

  override fun intents(): DetailIntents = object : DetailIntents {
    override fun navigation(): Observable<MenuItem> = viewNavigation.itemSelections()
    override fun args(): Single<Long> = Single.just(arguments.getLong(ARG_ROUTE_ID))
  }

  override fun render(state: DetailState) {
    viewAnimator.displayedChildId = state.view
    textRoute.text = state.route
    textRouteStats.text = state.stats
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