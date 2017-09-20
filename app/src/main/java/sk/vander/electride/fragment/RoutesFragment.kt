package sk.vander.electride.fragment

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import sk.vander.electride.R
import sk.vander.lib.ui.BaseFragment

/**
 * @author marian on 20.9.2017.
 */
class RoutesFragment : BaseFragment() {
  @BindView(R.id.recycler_routes) lateinit var routes: RecyclerView
  @BindView(R.id.fab_new_route) lateinit var fab: FloatingActionButton

  override fun layout(): Int = R.layout.screen_routes

  override fun onResume() {
    super.onResume()
    disposable.addAll(
        fab.clicks().subscribe {
          activity.fragmentManager.beginTransaction()
              .replace(R.id.container_id, RouteNewFragment())
              .addToBackStack("")
              .commit()
        }
    )
  }
}