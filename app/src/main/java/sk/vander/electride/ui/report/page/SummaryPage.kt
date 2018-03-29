package sk.vander.electride.ui.report.page

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import org.threeten.bp.LocalDate
import sk.vander.electride.R
import sk.vander.electride.ui.SummaryPageIntents
import sk.vander.electride.ui.SummaryPageState
import sk.vander.lib.ui.screen.Screen

class SummaryPage : Screen<SummaryPageModel, SummaryPageState, SummaryPageIntents>(SummaryPageModel::class) {
  @BindView(R.id.text_range_report) lateinit var range: TextView
  @BindView(R.id.text_route_report) lateinit var routes: TextView
  @BindView(R.id.fab_share) lateinit var fabShare: FloatingActionButton

  override fun layout(): Int = R.layout.page_summary

  override fun intents(): SummaryPageIntents = object : SummaryPageIntents {
    override fun args(): Single<Pair<LocalDate, LocalDate>> = arguments.let {
      Pair(it.getSerializable(ARG_FROM) as LocalDate, it.getSerializable(ARG_UNTIL) as LocalDate).toSingle()
    }

    override fun share(): Observable<Unit> = fabShare.clicks()
  }

  override fun render(state: SummaryPageState) {
    state.report?.let { range.text = it.toString() }
    routes.text = state.items.joinToString("\n\n")

  }

  companion object {
    const val ARG_FROM = "args_from"
    const val ARG_UNTIL = "args_until"

    fun newInstance(from: LocalDate, until: LocalDate): Screen<*, *, *> =
        SummaryPage().apply {
          arguments = Bundle(2).apply {
            putSerializable(ARG_FROM, from)
            putSerializable(ARG_UNTIL, until)
          }
        }
  }
}