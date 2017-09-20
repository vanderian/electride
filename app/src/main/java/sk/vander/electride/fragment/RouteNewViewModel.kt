package sk.vander.electride.fragment

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sk.vander.lib.debug.log
import javax.inject.Inject

/**
 * @author marian on 20.9.2017.
 */
class RouteNewViewModel @Inject constructor(private val rxLocation: RxLocation) {
  private val locationRequest: LocationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setInterval(5000L)

  @SuppressLint("MissingPermission")
  internal fun location(): Flowable<Location> = rxLocation.settings()
      .checkAndHandleResolution(locationRequest)
      .flatMapPublisher {
        return@flatMapPublisher if (it) {
          rxLocation.location().updates(locationRequest, BackpressureStrategy.LATEST)
        } else {
          Flowable.never<Location>()
        }
      }
      .log("has location:")
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
}
