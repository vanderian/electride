package sk.vander.electride.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RoutePointDao
import sk.vander.lib.annotations.ApplicationScope

/**
 * @author marian on 21.9.2017.
 */
@Module
object DataModule {

  @JvmStatic @Provides @ApplicationScope
  fun provideDatabase(context: Context): Database =
      Room.databaseBuilder(context, Database::class.java, "data.db")
          .build()

  @JvmStatic @Provides @ApplicationScope
  fun provideRouteDao(db: Database): RouteDao = db.routeDao()

  @JvmStatic @Provides @ApplicationScope
  fun provideRoutePointDao(db: Database): RoutePointDao = db.routePointDao()
}

