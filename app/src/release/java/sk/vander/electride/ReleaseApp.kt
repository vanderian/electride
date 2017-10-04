package sk.vander.electride

import android.util.Log
import autodagger.AutoInjector
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * @author marian on 15.11.2016.
 */
@AutoInjector(App::class)
class ReleaseApp : App() {
  override fun onCreate() {
    super.onCreate()
    Fabric.with(this, Crashlytics())
    Timber.plant(object : Timber.DebugTree() {
      override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        if (priority > Log.DEBUG) Crashlytics.log(priority, tag, message)
        t?.let { Crashlytics.logException(t) }
      }
    }, object : Timber.DebugTree() {
      override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= Log.INFO) {
          super.log(priority, tag, message, t)
        }
      }
    })
  }
}
