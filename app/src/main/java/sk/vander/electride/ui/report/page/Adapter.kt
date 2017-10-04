package sk.vander.electride.ui.report.page

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

class Adapter(fm: FragmentManager, val unit: ChronoUnit = ChronoUnit.MONTHS) : FragmentStatePagerAdapter(fm) {
  private val startDate = LocalDate.of(2000, 1, 1)
  private val endDate = LocalDate.of(2051, 1, 1)
  private val interval = startDate.until(endDate, unit)

  fun itemToDate(date: LocalDate): Int = startDate.until(date, unit).toInt()

  fun dateToItem(item: Int): Pair<LocalDate, LocalDate> = startDate.plus(item.toLong(), unit).let {
    Pair(it, it.plus(1, unit))
  }

  override fun getItem(position: Int): Fragment =
      startDate.plus(position.toLong(), unit).let {
        SummaryPage.newInstance(it, it.plus(1L, unit).minusDays(1L))
      }

  override fun getCount(): Int = interval.toInt()
}