package sk.vander.electride.ui.routes

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.entity.Route
import sk.vander.electride.service.LocationService
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

  internal fun onFab(activity: Activity): Function<FabMode, Maybe<Unit>> = Function {
    val intent = Intent(activity, LocationService::class.java)
    if (it == FabMode.STOP) {
      routeDao.queryCompleted().firstOrError()
          .flatMapMaybe { Maybe.fromCallable { routeDao.update(it.first().copy(completed = true)) } }
    } else {
      RxPermissions(activity).request(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
          .firstOrError().filter { it }
          .doOnSuccess { ContextCompat.startForegroundService(activity, intent) }
          .flatMap { Maybe.fromAction<Unit> { routeDao.insert(Route.new()) }.subscribeOn(Schedulers.io()) }
    }
  }

  enum class FabMode {
    START, STOP
  }

}
