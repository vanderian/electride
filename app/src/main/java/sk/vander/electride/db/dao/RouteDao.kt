package sk.vander.electride.db.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import sk.vander.electride.db.entity.Route
import sk.vander.electride.db.entity.RouteWithStats

/**
 * @author marian on 21.9.2017.
 */
@Dao
interface RouteDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insert(route: Route): Long

  @Update
  fun update(route: Route)

/*
  @Query("SELECT * FROM routes WHERE completed = :completed")
  fun queryCompleted(completed: Boolean = false): Flowable<List<Route>>

  @Query("SELECT * FROM routes WHERE completed = :completed")
  fun hasCompleted(completed: Boolean = false): Maybe<Route>
*/

  @Query("SELECT * FROM routes WHERE id = :id")
  fun queryOne(id: Long): Flowable<Route>

  @Query("SELECT * FROM routes")
  fun queryAll(): Flowable<List<Route>>

  @Query("SELECT routes.id, name, date, recurrence, distance, duration, waypoints" +
      " FROM routes" +
      " LEFT JOIN route_stats ON route_stats.routeId = routes.id")
  fun queryAllWithStats(): Flowable<List<RouteWithStats>>

  @Query("SELECT routes.id, name, date, recurrence, distance, duration, waypoints" +
      " FROM routes" +
      " LEFT JOIN route_stats ON route_stats.routeId = routes.id" +
      " WHERE (recurrence = 0 AND date BETWEEN :from AND :to)" +
      " OR (NOT recurrence = 0 AND date <= :to)")
  fun queryWithStats(from: LocalDate, to: LocalDate): Flowable<List<RouteWithStats>>

}