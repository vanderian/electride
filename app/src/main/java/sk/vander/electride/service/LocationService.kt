package sk.vander.electride.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import dagger.android.AndroidInjection
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sk.vander.electride.MainActivity
import sk.vander.electride.R
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RoutePointDao
import sk.vander.electride.db.entity.Route
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

  private fun setForeground(route: Route) {
    val pending = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
    val notification = NotificationCompat.Builder(this, DEFAULT_CHANNEL)
        .setContentTitle(getText(R.string.notification_location_title))
        .setContentText(route.toString())
        .setSmallIcon(R.drawable.ic_location_searching_black_24dp)
        .setTicker(getText(R.string.notification_location_ticker))
        .setContentIntent(pending)
        .build()
    startForeground(ONGOING_ID, notification)
  }

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
              .distinctUntilChanged()
              .doOnNext { setForeground(it.first()) }
              .map { it.first().id }
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

  companion object {
    const val ONGOING_ID = 6123
    const val DEFAULT_CHANNEL = "miscellaneous"
  }
}