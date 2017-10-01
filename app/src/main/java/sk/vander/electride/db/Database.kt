package sk.vander.electride.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RouteStatsDao
import sk.vander.electride.db.dao.TrackPointDao
import sk.vander.electride.db.entity.Route
import sk.vander.electride.db.entity.RouteStats
import sk.vander.electride.db.entity.TrackPoint

/**
 * @author marian on 21.9.2017.
 */
@Database(entities = arrayOf(
    Route::class,
    TrackPoint::class,
    RouteStats::class
), version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
  abstract fun routeDao(): RouteDao
  abstract fun trackPointDao(): TrackPointDao
  abstract fun routeStatsDao(): RouteStatsDao
}