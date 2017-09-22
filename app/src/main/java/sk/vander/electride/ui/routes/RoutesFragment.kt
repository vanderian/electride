package sk.vander.electride.ui.routes

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import sk.vander.electride.R
import sk.vander.electride.ui.routes.RoutesViewModel.FabMode.START
import sk.vander.lib.ui.BaseFragment

/**
 * @author marian on 20.9.2017.
 */
class RoutesFragment : BaseFragment<RoutesViewModel>() {
  @BindView(R.id.recycler_routes) lateinit var routes: RecyclerView
  @BindView(R.id.fab_new_route) lateinit var fab: FloatingActionButton

  override fun getViewModelClass(): Class<RoutesViewModel> = RoutesViewModel::class.java
  override fun layout(): Int = R.layout.screen_routes

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    routes.layoutManager = LinearLayoutManager(context)
    routes.adapter = viewModel.adapter
  }

  override fun onStart() {
    super.onStart()
    disposable.addAll(
        fab.clicks()
            .map { fab.tag as RoutesViewModel.FabMode }
            .flatMapMaybe(viewModel.onFab(activity))
            .subscribe(),

        viewModel.fabMode()
            .doOnNext { fab.tag = it }
            .map { if (it == START) R.drawable.ic_add_black_24dp else R.drawable.ic_clear_black_24dp }
            .subscribe { res -> fab.setImageResource(res) }
    )
  }
}