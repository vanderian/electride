package sk.vander.electride.db.entity

import com.mapbox.services.api.directions.v5.models.DirectionsWaypoint

data class Waypoint(
    val name: String,
    val latitude: Double,
    val longitude: Double
) {
  constructor(waypoint: DirectionsWaypoint): this(waypoint.name, waypoint.asPosition().latitude, waypoint.asPosition().longitude)
}