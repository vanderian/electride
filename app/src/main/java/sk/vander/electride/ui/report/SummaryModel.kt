package sk.vander.electride.ui.report

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.threeten.bp.LocalDate
import sk.vander.electride.ui.SummaryIntents
import sk.vander.electride.ui.SummaryState
import sk.vander.lib.ui.screen.GoBack
import sk.vander.lib.ui.screen.Result
import sk.vander.lib.ui.screen.ScreenModel
import javax.inject.Inject

class SummaryModel @Inject constructor() : ScreenModel<SummaryState, SummaryIntents>(SummaryState()) {
  override fun collectIntents(intents: SummaryIntents, result: Observable<Result>): Disposable =
      Observable.merge(
          intents.toolbarMenu().doOnNext { state.next { copy(date = LocalDate.now()) } },
          intents.toolbarNav().doOnNext { navigation.onNext(GoBack) }
      ).subscribe()
}