package sk.vander.electride.ui.routes

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import sk.vander.electride.R
import sk.vander.electride.db.entity.Route
import sk.vander.electride.ui.RouteIntents
import sk.vander.electride.ui.routes.adapter.RouteItem
import sk.vander.electride.ui.routes.adapter.RouteViewHolder
import sk.vander.electride.ui.visibility
import sk.vander.lib.ui.screen.ListState
import sk.vander.lib.ui.screen.Screen
import sk.vander.lib.ui.widget.adapter.ListSource
import sk.vander.lib.ui.widget.adapter.RecyclerAdapter

/**
 * @author marian on 20.9.2017.
 */
class RoutesScreen : Screen<RoutesModel, ListState<RouteItem>, RouteIntents>(RoutesModel::class) {
  private val source = ListSource<RouteItem>()
  val adapter = RecyclerAdapter(source, { _, view -> RouteViewHolder(view) })

  @BindView(R.id.recycler_routes) lateinit var routes: RecyclerView
  @BindView(R.id.fab_new_route) lateinit var fab: FloatingActionButton
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.view_progress) lateinit var progress: View
  @BindView(R.id.view_empty) lateinit var empty: View

  override fun layout(): Int = R.layout.screen_routes

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    routes.layoutManager = LinearLayoutManager(context)
    routes.adapter = adapter
    toolbar.inflateMenu(R.menu.menu_fab_routes)
    toolbar.setTitle(R.string.label_routes)
  }

  override fun intents(): RouteIntents = object : RouteIntents {
    override fun newRoute(): Observable<Unit> = fab.clicks()
    override fun routeSelected(): Observable<Route> = adapter.itemEventSource.toObservable()
  }

  override fun render(state: ListState<RouteItem>) {
    if (state.loading) fab.hide() else fab.show()
    progress.visibility = state.loading.visibility()
    empty.visibility = state.empty.visibility()
    source.setList(state.items)
  }
}