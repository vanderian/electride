package sk.vander.electride.db.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import sk.vander.electride.db.entity.Route

/**
 * @author marian on 21.9.2017.
 */
@Dao
interface RouteDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  fun insert(route: Route): Long

  @Update
  fun update(route: Route)

  @Query("SELECT * FROM routes WHERE completed = :completed")
  fun queryCompleted(completed: Boolean = false): Flowable<List<Route>>

  @Query("SELECT * FROM routes WHERE completed = :completed")
  fun hasCompleted(completed: Boolean = false): Maybe<Route>

  @Query("SELECT * FROM routes WHERE id = :id")
  fun queryOne(id: Long): Flowable<Route>

  @Query("SELECT * FROM routes")
  fun queryAll(): Flowable<List<Route>>
}