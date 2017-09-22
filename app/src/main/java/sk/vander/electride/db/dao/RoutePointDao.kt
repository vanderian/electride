package sk.vander.electride.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import sk.vander.electride.db.entity.RoutePoint

/**
 * @author marian on 21.9.2017.
 */
@Dao
interface RoutePointDao {

  @Insert
  fun insert(routePoint: RoutePoint)

  @Query("SELECT * FROM route_points WHERE routeId = :routeId")
  fun queryRoute(routeId: Long): Flowable<List<RoutePoint>>
}