package sk.vander.electride.ui.routes

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.entity.Route
import sk.vander.electride.ui.routes.adapter.RouteItem
import sk.vander.electride.ui.routes.adapter.RouteViewHolder
import sk.vander.lib.ui.widget.adapter.ListSource
import sk.vander.lib.ui.widget.adapter.RecyclerAdapter
import javax.inject.Inject

/**
 * @author marian on 21.9.2017.
 */
class RoutesViewModel @Inject constructor(
    private val routeDao: RouteDao
) : ViewModel() {

  private val disposable = CompositeDisposable()
  private val source = ListSource<RouteItem>()
  val adapter = RecyclerAdapter(source, { _, view -> RouteViewHolder(view) })

  init {
    disposable.addAll(
        routeDao.queryAll().map { it.map { RouteItem(it) } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { source.setList(it) }
    )
  }

  override fun onCleared() {
    disposable.clear()
  }

  internal fun fabMode(): Flowable<FabMode> = routeDao.queryCompleted()
      .map { if (it.isEmpty()) FabMode.START else FabMode.STOP }
      .observeOn(AndroidSchedulers.mainThread())

  internal fun onStop() = routeDao.queryCompleted().firstOrError()
      .flatMapCompletable { Completable.fromAction { routeDao.update(it.first().copy(completed = true)) } }

  internal fun onStart() = Completable.fromAction { routeDao.insert(Route.new()) }.subscribeOn(Schedulers.io())

  enum class FabMode {
    START, STOP
  }

}
