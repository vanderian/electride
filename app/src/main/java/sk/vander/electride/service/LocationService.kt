package sk.vander.electride.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import dagger.android.AndroidInjection
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RoutePointDao
import sk.vander.electride.db.entity.RoutePoint
import sk.vander.lib.debug.log
import javax.inject.Inject

/**
 * @author marian on 21.9.2017.
 */
class LocationService : Service() {
  private val disposable = CompositeDisposable()
  private val locationRequest: LocationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setInterval(5000L)

  @Inject lateinit var routeDao: RouteDao
  @Inject lateinit var routePointDao: RoutePointDao
  @Inject lateinit var rxLocation: RxLocation

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  @SuppressLint("MissingPermission")
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (disposable.size() == 0) {
      disposable.addAll(
          routeDao.queryCompleted()
              .doOnNext { if (it.isEmpty()) stopSelf() }
              .filter { it.size == 1 }
              .log("has route")
              .map { it.first().id }
              .distinctUntilChanged()
              .switchMap { id ->
                rxLocation.settings().checkAndHandleResolution(locationRequest)
                    .filter { it }
                    .flatMapPublisher {
                      rxLocation.location().updates(locationRequest, BackpressureStrategy.LATEST)
                          .observeOn(Schedulers.io())
                    }
                    .log("has location:")
                    .doOnNext { routePointDao.insert(RoutePoint(it, id)) }
              }
              .subscribeOn(Schedulers.io())
              .doOnCancel { stopSelf() }
              .subscribe()
      )
    }
    return START_NOT_STICKY
  }

  override fun onDestroy() {
    disposable.clear()
    super.onDestroy()
  }
}