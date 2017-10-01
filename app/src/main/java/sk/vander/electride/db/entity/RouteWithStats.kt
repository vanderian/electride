package sk.vander.electride.db.entity

import org.threeten.bp.LocalDate
import sk.vander.electride.ui.Recurrence

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
}