package sk.vander.electride.db.entity

import android.arch.persistence.room.*
import com.mapbox.services.api.directions.v5.models.DirectionsResponse

@Entity(tableName = "route_stats",
    indices = arrayOf(Index("routeId")),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Route::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("routeId"),
            onDelete = ForeignKey.CASCADE,
            deferred = true
        ))
)
data class RouteStats(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routeId: Long,
    val distance: Double,
    val duration: Double,
    val geometry: String,
    @ColumnInfo(name = "waypoints")
    val waypoints: List<Waypoint>
) {
  companion object {
    fun create(routeId: Long, res: DirectionsResponse) =
        RouteStats(0, routeId, res.routes.single().distance, res.routes.single().duration,
            res.routes.single().geometry, res.waypoints.map { Waypoint(it) })
  }
}