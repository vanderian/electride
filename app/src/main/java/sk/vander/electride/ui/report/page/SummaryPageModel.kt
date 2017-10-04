package sk.vander.electride.ui.report.page

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.dao.RouteStatsDao
import sk.vander.electride.db.entity.RouteWithStats
import sk.vander.electride.ui.Recurrence
import sk.vander.electride.ui.SummaryPageIntents
import sk.vander.electride.ui.SummaryPageState
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

class SummaryPageModel @Inject constructor(
    private val routeDao: RouteDao,
    private val routeStatsDao: RouteStatsDao
) : ScreenModel<SummaryPageState, SummaryPageIntents>(SummaryPageState()) {

  override fun collectIntents(intents: SummaryPageIntents, result: Observable<Result>): Disposable =
      intents.args()
          .flatMapPublisher { range ->
            routeDao.queryWithStats(range.first, range.second)
                .map { it.map { it.to(generate(range)(it)) }.filterNot { it.second.isEmpty() } }
          }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnNext { state.next { copy(items = it) } }
          .subscribe()

//  private fun lazySeq(initial: RouteWithStats, chronoUnit: ChronoUnit): Sequence<RouteWithStats> = buildSequence {
//    yield(initial)
//    while (true) yield(initial.copy(date = in))
//  }

  private fun generate(range: Pair<LocalDate, LocalDate>): (RouteWithStats) -> List<RouteWithStats> = {
    when (it.recurrence) {
      Recurrence.NONE -> listOf(it)
      Recurrence.WEEKDAYS -> it.generateForType(range)
          .filterNot { it.date.dayOfWeek == DayOfWeek.SATURDAY || it.date.dayOfWeek == DayOfWeek.SUNDAY }
      else -> it.generateForType(range)
    }
  }

  private fun RouteWithStats.generateForType(range: Pair<LocalDate, LocalDate>): List<RouteWithStats> {
      val initial = if (date.isBefore(range.first)) {
        var newDate = date.plus(date.until(range.first, recurrence.unit), recurrence.unit)
        newDate = if (newDate.isWithin(range)) newDate else newDate.plus(1, recurrence.unit)
        if (newDate.isWithin(range)) copy(date = newDate) else null
      } else {
        this@generateForType
      }
    return generateSequence(initial, {
      val newDate = it.date.plus(1, recurrence.unit)
      if (newDate.isWithin(range)) it.copy(date = newDate) else null
    }).toList()
  }

  private fun LocalDate.isWithin(range: Pair<LocalDate, LocalDate>) =
      isEqual(range.first) || isEqual(range.second) || (isAfter(range.first) && isBefore(range.second))
}