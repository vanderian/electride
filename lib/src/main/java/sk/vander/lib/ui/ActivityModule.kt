package sk.vander.lib.ui

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import sk.vander.lib.annotations.ActivityScope

@Module
@ActivityScope
class ActivityModule(private val activity: Activity) {

  @Provides fun provideContext(): Context = activity
}
