package sk.vander.electride

import android.os.StrictMode
import autodagger.AutoInjector
import com.facebook.stetho.Stetho
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import timber.log.Timber

/**
 * @author marian on 15.11.2016.
 */
@AutoInjector(App::class)
class DebugApp : App() {
  private fun setupAppMonitoring() {
    LeakCanary.install(this)
    BlockCanary.install(this, BlockCanaryContext()).start()
  }

  override fun onCreate() {
    Timber.plant(Timber.DebugTree())
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }
    super.onCreate()
    Stetho.initializeWithDefaults(this)

    setupAppMonitoring()
//    ButterKnife.setDebug(true)
    Picasso.setSingletonInstance(Picasso.Builder(this).loggingEnabled(true).build())

    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().penaltyDeath().detectAll().build())
    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().penaltyDeath().detectAll().build())
  }
}
