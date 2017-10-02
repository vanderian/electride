package sk.vander.electride.data

import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.Reusable
import sk.vander.lib.annotations.ApplicationScope
import javax.inject.Qualifier

@Module
object DataModule {

  @JvmStatic @Provides @ApplicationScope
  fun providePreferences(context: Context): RxSharedPreferences =
      RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(context))

  @JvmStatic @Provides @Reusable @RangePref
  fun provideRangePref(rxSharedPreferences: RxSharedPreferences): Preference<String> =
      rxSharedPreferences.getString("pref_range")

  @JvmStatic @Provides @Reusable @OffRoutePref
  fun provideOffRouteDistancePref(rxSharedPreferences: RxSharedPreferences): Preference<String> =
      rxSharedPreferences.getString("pref_chargers_offroute")

}

@Qualifier annotation class RangePref
@Qualifier annotation class OffRoutePref