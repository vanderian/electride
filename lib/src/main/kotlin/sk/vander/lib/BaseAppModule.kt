package sk.vander.lib

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import sk.vander.lib.annotations.ApplicationScope

@Module(
    includes = arrayOf(AndroidInjectionModule::class)
)
class BaseAppModule(private val app: Application) {

  @Provides @ApplicationScope fun provideApplication(): Application = app

  @Provides @ApplicationScope fun provideApplicationContext(): Context = app.applicationContext

  @Provides @ApplicationScope fun provideResources(): Resources = app.resources

}
