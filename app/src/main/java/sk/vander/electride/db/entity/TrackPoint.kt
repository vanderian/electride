package sk.vander.electride.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.location.Location

/**
 * @author marian on 21.9.2017.
 */
@Entity(
    tableName = "track_points",
    indices = arrayOf(Index("routeId")),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Route::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("routeId"),
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )))
data class TrackPoint(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val routeId: Long,
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Float,
    val bearing: Float,
    val accuracy: Float
) {
  constructor(l: Location, routeId: Long) :
      this(0, routeId, l.time, l.latitude, l.longitude, l.altitude, l.speed, l.bearing, l.accuracy)
}
