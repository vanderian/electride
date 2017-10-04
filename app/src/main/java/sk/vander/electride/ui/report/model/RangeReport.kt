package sk.vander.electride.ui.report.model

import sk.vander.electride.ui.toDmhs
import sk.vander.electride.ui.toKm

data class RangeReport(
    val distance: Double,
    val duration: Double,
    val routes: Int,
    val recharges: Int,
    val range: Int
) {
  constructor(routes: List<RouteReport>, range: Int): this(
      routes.sumByDouble { it.totalDistance },
      routes.sumByDouble { it.totalDuration },
      routes.sumBy { it.numRoutes },
      routes.sumBy { it.recharges },
      range
  )

  override fun toString(): String {
    return "A Total distance of ${distance.toKm()}, has taken ${duration.toDmhs()}, " +
        "on $routes routes with $recharges recharges for a $range km range"
  }
}