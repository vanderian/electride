package sk.vander.lib.ui.screen

import io.reactivex.processors.BehaviorProcessor

/**
 * @author marian on 9.8.2017.
 */

abstract class StateViewModel<T> {
  protected val state: BehaviorProcessor<T> = BehaviorProcessor.create<T>()

  @Suppress("UNCHECKED_CAST")
  internal fun state(key: Any) {
    state.onNext(key as T)
  }
}
