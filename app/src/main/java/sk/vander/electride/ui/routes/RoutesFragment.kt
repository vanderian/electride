package sk.vander.electride.ui.routes

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import sk.vander.electride.R
import sk.vander.electride.service.LocationService
import sk.vander.electride.ui.routes.directions.DirectionsFragment
import sk.vander.electride.ui.routes.RoutesViewModel.FabMode.START
import sk.vander.electride.ui.routes.RoutesViewModel.FabMode.STOP
import sk.vander.electride.ui.routes.detail.RouteDetailFragment
import sk.vander.lib.ui.BaseFragment

/**
 * @author marian on 20.9.2017.
 */
class RoutesFragment : BaseFragment<RoutesViewModel>(RoutesViewModel::class) {
  @BindView(R.id.recycler_routes) lateinit var routes: RecyclerView
  @BindView(R.id.fab_new_route) lateinit var fab: FloatingActionButton
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

  override fun layout(): Int = R.layout.screen_routes

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    routes.layoutManager = LinearLayoutManager(context)
    routes.adapter = viewModel.adapter
    toolbar.inflateMenu(R.menu.menu_fab_routes)
    toolbar.setTitle(R.string.label_routes)
  }

  override fun onStart() {
    super.onStart()
    disposable.addAll(
        fab.clicks()
            .map { fab.tag as RoutesViewModel.FabMode }
            .flatMapCompletable {
              when (it) {
                STOP -> viewModel.onStop()
                START -> RxPermissions(activity).request(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
                    .firstOrError().filter { it }
                    .map { Intent(context, LocationService::class.java) }
                    .doOnSuccess { ContextCompat.startForegroundService(context, it) }
                    .flatMapCompletable { viewModel.onStart() }
              }
            }
            .subscribe(),
        toolbar.itemClicks()
            .filter { it.itemId == R.id.action_from_map }
            .doOnNext {
              activity.supportFragmentManager.beginTransaction()
                  .replace(R.id.container_id, DirectionsFragment())
                  .addToBackStack(null)
                  .commit()
            }
            .subscribe(),

        viewModel.fabMode()
            .doOnNext { fab.tag = it }
            .map { if (it == START) R.drawable.ic_add_black_24dp else R.drawable.ic_clear_black_24dp }
            .subscribe { res -> fab.setImageResource(res) },
        viewModel.adapter
            .itemEventSource
            .subscribe {
              activity.supportFragmentManager.beginTransaction()
                  .replace(R.id.container_id, RouteDetailFragment.newInstance(it.id))
                  .addToBackStack(null)
                  .commit()
            }
    )
  }
}