package sk.vander.electride.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import sk.vander.electride.db.entity.RoutePoint

/**
 * @author marian on 21.9.2017.
 */
@Dao
interface RoutePointDao {

  @Insert
  fun insert(routePoint: RoutePoint)
}