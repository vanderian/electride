package sk.vander.electride.ui.report.page

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
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
                .map { generateRecurrent(range, it) }
          }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnNext { state.next { copy(items = it) } }
          .subscribe()

//  private fun lazySeq(initial: RouteWithStats, chronoUnit: ChronoUnit): Sequence<RouteWithStats> = buildSequence {
//    yield(initial)
//    while (true) yield(initial.copy(date = in))
//  }

  private fun generateRecurrent(range: Pair<LocalDate, LocalDate>, list: List<RouteWithStats>): List<RouteWithStats> {
    val grouped = list.groupBy { it.recurrence }
    val once = grouped[Recurrence.NONE] ?: emptyList()
    val daily = grouped[Recurrence.DAILY]?.map {
      val initial = if (it.date.isBefore(range.first)) it.copy(date = range.first) else it
      generateSequence(initial, { if (it.date.isBefore(range.second)) it.copy(date = it.date.plus(1, ChronoUnit.DAYS)) else null })
          .toList()
    }?.flatten() ?: emptyList()
    val weekdays = daily.filterNot { it.date.dayOfWeek == DayOfWeek.SATURDAY || it.date.dayOfWeek == DayOfWeek.SUNDAY }
    val weekly = grouped[Recurrence.WEEKLY]?.map {
      val initial = if (it.date.isBefore(range.first)) it.copy(date = range.first) else it
      generateSequence(initial, { if (it.date.isBefore(range.second)) it.copy(date = it.date.plus(1, ChronoUnit.DAYS)) else null })
          .toList()
    }?.flatten() ?: emptyList()
    val monthly = grouped[Recurrence.DAILY]?.map {
      val initial = if (it.date.isBefore(range.first)) it.copy(date = range.first) else it
      generateSequence(initial, { if (it.date.isBefore(range.second)) it.copy(date = it.date.plus(1, ChronoUnit.DAYS)) else null })
          .toList()
    }?.flatten() ?: emptyList()
    val yearly = grouped[Recurrence.DAILY]?.map {
      val initial = if (it.date.isBefore(range.first)) it.copy(date = range.first) else it
      generateSequence(initial, { if (it.date.isBefore(range.second)) it.copy(date = it.date.plus(1, ChronoUnit.DAYS)) else null })
          .toList()
    }?.flatten() ?: emptyList()
    return once.plus(daily)
  }
}