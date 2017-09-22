package sk.vander.electride.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RoutePointDao
import sk.vander.electride.db.entity.Route
import sk.vander.electride.db.entity.RoutePoint

/**
 * @author marian on 21.9.2017.
 */
@Database(entities = arrayOf(Route::class, RoutePoint::class), version = 1)
abstract class Database : RoomDatabase() {
  abstract fun routeDao(): RouteDao
  abstract fun routePointDao(): RoutePointDao
}