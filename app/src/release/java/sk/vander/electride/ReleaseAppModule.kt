package sk.vander.electride

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import sk.vander.lib.ui.ActivityHierarchyServer
import sk.vander.lib.ui.ViewContainer

/**
 * @author marian on 5.9.2017.
 */
@Module(includes = arrayOf(App.Module::class))
object ReleaseAppModule {

  @JvmStatic @Provides fun providesViewContainer(): ViewContainer = ViewContainer.DEFAULT
  @JvmStatic @Provides fun providesHierarchyServer(): ActivityHierarchyServer = ActivityHierarchyServer.NONE
  @JvmStatic @Provides fun providesOkhttpBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()

}