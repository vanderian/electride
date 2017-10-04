package sk.vander.electride.ui.report

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import io.reactivex.Observable
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoUnit
import sk.vander.electride.R
import sk.vander.electride.ui.SummaryIntents
import sk.vander.electride.ui.SummaryState
import sk.vander.electride.ui.report.page.Adapter
import sk.vander.lib.ui.screen.Screen
import java.util.*

class SummaryScreen : Screen<SummaryModel, SummaryState, SummaryIntents>(SummaryModel::class) {
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.view_pager) lateinit var pager: ViewPager

  override fun layout(): Int = R.layout.screen_summary

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
      override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        toolbar.title = pager.title(position)
        toolbar.subtitle = pager.subtitle(position)
      }
    })
    pager.adapter = Adapter(childFragmentManager)
    toolbar.inflateMenu(R.menu.menu_summary)
  }

  override fun intents(): SummaryIntents = object : SummaryIntents {
    override fun toolbarNav(): Observable<Unit> = toolbar.navigationClicks()
    override fun toolbarMenu(): Observable<MenuItem> = toolbar.itemClicks().share()
  }

  override fun render(state: SummaryState) {
    pager.currentItem(state.date)
  }

  private fun ViewPager.currentItem(date: LocalDate) {
    currentItem = (adapter as Adapter).itemToDate(date)
  }

  private fun ViewPager.title(position: Int): String =
      (adapter as Adapter).let {
        val range = it.dateToItem(position)
        when (it.unit) {
          ChronoUnit.MONTHS -> range.first.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
          else -> range.toString()
        }
      }

  private fun ViewPager.subtitle(position: Int): String =
      (adapter as Adapter).let {
        val date = it.dateToItem(position).first
        if (YearMonth.now().year != date.year) date.year.toString() else ""
      }
}