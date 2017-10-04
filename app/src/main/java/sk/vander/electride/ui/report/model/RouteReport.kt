package sk.vander.electride.ui.report.model

import org.threeten.bp.LocalDate
import sk.vander.electride.db.entity.RouteWithStats
import sk.vander.electride.ui.Recurrence
import sk.vander.electride.ui.recharges
import sk.vander.electride.ui.toDmhs
import sk.vander.electride.ui.toKm

data class RouteReport(
    val id: Long,
    val name: String,
    val date: LocalDate,
    val recurrence: Recurrence,
    val distance: Double,
    val duration: Double,
    val totalDistance: Double,
    val totalDuration: Double,
    val recharges: Int,
    val dates: List<LocalDate>
) {
  constructor(entry: Pair<RouteWithStats, List<RouteWithStats>>, range: Int) : this(
      entry.first.id,
      entry.first.name,
      entry.first.date,
      entry.first.recurrence,
      entry.first.distance,
      entry.first.duration,
      entry.second.sumByDouble { it.distance },
      entry.second.sumByDouble { it.duration },
      entry.first.distance.recharges(range),
      entry.second.map { it.date }
  )

  val numRoutes: Int
    get() = dates.size

  override fun toString(): String {
    return "RouteReport(id=$id, name='$name', date=$date, recurrence=$recurrence, distance=${distance.toKm()}," +
        " duration=${duration.toDmhs()}, totalDistance=${totalDistance.toKm()}," +
        " totalDuration=${totalDuration.toDmhs()}, recharges=$recharges, dates=$dates)"
  }


}