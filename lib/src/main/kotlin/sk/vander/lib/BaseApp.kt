package sk.vander.lib

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import sk.vander.lib.ui.ActivityHierarchyServer
import timber.log.Timber
import javax.inject.Inject

abstract class BaseApp : DaggerApplication() {
  @Inject lateinit var activityHierarchyServer: ActivityHierarchyServer

  protected abstract fun buildComponentAndInject()

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return AndroidInjector { buildComponentAndInject() }
  }

  override fun onCreate() {
    super.onCreate()

    registerActivityLifecycleCallbacks(activityHierarchyServer)

    RxJavaPlugins.setErrorHandler { Timber.e(it, "Uncaught RxJava error") }
  }
}
