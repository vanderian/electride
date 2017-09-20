package sk.vander.lib.ui

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable

/**
 * @author marian on 20.9.2017.
 */
abstract class BaseFragment: Fragment() {
  val disposable = CompositeDisposable()
  private lateinit var unbinder: Unbinder

  @LayoutRes abstract fun layout(): Int

  override fun onAttach(context: Context) {
    AndroidInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(layout(), container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = ButterKnife.bind(this, view)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onPause() {
    disposable.clear()
    super.onPause()
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    unbinder.unbind()
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  override fun onDetach() {
    super.onDetach()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
  }
}