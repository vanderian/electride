package sk.vander.electride.ui.report.page

import android.content.Intent
import com.f2prateek.rx.preferences2.Preference
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import sk.vander.electride.data.RangePref
import sk.vander.electride.db.dao.RouteDao
import sk.vander.electride.db.entity.RouteWithStats
import sk.vander.electride.ui.Recurrence
import sk.vander.electride.ui.SummaryPageIntents
import sk.vander.electride.ui.SummaryPageState
import sk.vander.electride.ui.report.model.RangeReport
import sk.vander.electride.ui.report.model.RouteReport
import sk.vander.lib.ui.screen.NextIntent
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject


class SummaryPageModel @Inject constructor(
    private val routeDao: RouteDao,
    @RangePref private val rangePref: Preference<String>
) : ScreenModel<SummaryPageState, SummaryPageIntents>(SummaryPageState()) {

  override fun collectIntents(intents: SummaryPageIntents, result: Observable<Result>): Disposable =
      CompositeDisposable().apply {
        add(intents.args()
            .flatMapPublisher { range ->
              routeDao.queryWithStats(range.first, range.second)
                  .map { it.map { it.to(generate(range)(it)) }.filterNot { it.second.isEmpty() } }
                  .map { it.map { RouteReport(it, rangePref.get().toInt()) } }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { state.next { copy(items = it, report = RangeReport(it, rangePref.get().toInt())) } }
            .subscribe())

        add(
            intents.share()
                .map { Gson().toJson(state.value) }
                .map {
                  Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, it)
                    type = "text/plain"
                  }
                }
                .doOnNext { navigation.onNext(NextIntent(it)) }
                .subscribe()
        )
      }

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