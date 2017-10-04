package sk.vander.electride.db.entity

import org.threeten.bp.LocalDate
import sk.vander.electride.ui.Recurrence
import sk.vander.electride.ui.toDmhs
import sk.vander.electride.ui.toKm

data class RouteWithStats(
    val id: Long,
    val name: String,
    val date: LocalDate,
    val recurrence: Recurrence,
    val distance: Double,
    val duration: Double
//    val waypoints: List<Waypoint>
) {
  fun route() = Route(id, name, date, recurrence)

  override fun toString() =
      "Id=$id, Name=$name, Date=$date, Recurrence=$recurrence, " +
          "Distance=${distance.toKm()}, " +
          "Duration=${duration.toDmhs()}"
}