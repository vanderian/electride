package sk.vander.lib.ui.screen

import android.view.View

import flow.Flow

/**
 * @author marian on 9.8.2017.
 */

abstract class StateCoordinator<out T : StateViewModel<*>>(protected val viewModel: T) : BaseCoordinator() {

  override fun attach(view: View) {
    super.attach(view)
    Flow.getKey<Any>(view)?.let(viewModel::state)
  }
}
