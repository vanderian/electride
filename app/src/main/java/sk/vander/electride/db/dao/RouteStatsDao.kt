package sk.vander.electride.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import sk.vander.electride.db.entity.RouteStats

@Dao
interface RouteStatsDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insert(routeStats: RouteStats): Long

  @Query("SELECT * FROM route_stats WHERE routeId = :routeId")
  fun queryRoute(routeId: Long): Flowable<List<RouteStats>>
}