package sk.vander.electride.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import sk.vander.electride.db.entity.TrackPoint

/**
 * @author marian on 21.9.2017.
 */
@Dao
interface TrackPointDao {

  @Insert
  fun insert(trackPoint: TrackPoint)

  @Query("SELECT * FROM track_points WHERE routeId = :routeId")
  fun queryRoute(routeId: Long): Flowable<List<TrackPoint>>
}