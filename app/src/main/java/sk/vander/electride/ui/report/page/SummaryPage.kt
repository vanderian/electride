package sk.vander.electride.ui.report.page

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import org.threeten.bp.LocalDate
import sk.vander.electride.R
import sk.vander.electride.ui.SummaryPageIntents
import sk.vander.electride.ui.SummaryPageState
import sk.vander.lib.ui.screen.Screen

class SummaryPage : Screen<SummaryPageModel, SummaryPageState, SummaryPageIntents>(SummaryPageModel::class) {
  @BindView(R.id.text_range) lateinit var range: TextView

  override fun layout(): Int = R.layout.page_summary

  override fun intents(): SummaryPageIntents = object : SummaryPageIntents {
    override fun args(): Single<Pair<LocalDate, LocalDate>> = arguments.let {
      Pair(it.getSerializable(ARG_FROM) as LocalDate, it.getSerializable(ARG_UNTIL) as LocalDate).toSingle()
    }
  }

  override fun render(state: SummaryPageState) {
    range.text = state.items.joinToString("\n\n")
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