package sk.vander.electride.db.entity

import android.arch.persistence.room.*
import com.mapbox.services.api.directions.v5.models.DirectionsResponse
import sk.vander.electride.ui.format
import java.util.concurrent.TimeUnit

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

  override fun toString() =
      "Distance=${distance.div(1000).format(2)} km, " +
          "Duration=${TimeUnit.SECONDS.toMinutes(duration.toLong())} min,\n\n" +
          waypoints.joinToString("\n\n")

  companion object {

    fun create(routeId: Long, res: DirectionsResponse) =
        RouteStats(0, routeId, res.routes.single().distance, res.routes.single().duration,
            res.routes.single().geometry, res.waypoints.map { Waypoint(it) })

  }
}