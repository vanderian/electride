package sk.vander.electride

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mapbox.mapboxsdk.Mapbox
import com.patloew.rxlocation.RxLocation
import dagger.Provides
import io.fabric.sdk.android.Fabric
import sk.vander.lib.BaseApp
import sk.vander.lib.BaseAppModule
import sk.vander.lib.annotations.ApplicationScope
import timber.log.Timber

/**
 * @author marian on 5.9.2017.
 */
@BuildTypeComponent
abstract class App : BaseApp() {
  override fun buildComponentAndInject() = Initializer.init(this).inject(this)

  override fun onCreate() {
    super.onCreate()
    Mapbox.getInstance(this, BuildConfig.MAPBOX_TOKEN)
    AndroidThreeTen.init(this)
    PreferenceManager.setDefaultValues(this, R.xml.preferences, true)
    Fabric.with(this, Crashlytics())
    Timber.plant(object : Timber.DebugTree() {
      override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        if (priority > Log.DEBUG) Crashlytics.log(priority, tag, message)
        t?.let { Crashlytics.logException(t) }
      }
    })
  }

  @dagger.Module(includes = arrayOf(BaseAppModule::class))
  object Module {
    @JvmStatic @Provides @ApplicationScope
    fun provideRxLocation(context: Context): RxLocation = RxLocation(context)
  }
}