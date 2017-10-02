package sk.vander.lib.ui.screen

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import sk.vander.lib.Injectable
import sk.vander.lib.R
import sk.vander.lib.debug.log
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * @author marian on 20.9.2017.
 */
abstract class Screen<T : ScreenModel<U, V>, U : Screen.State, V: Screen.Intents>(
    private val clazz: KClass<T>
) : Fragment(), Injectable {

  interface State
  interface Intents

  private val result = BehaviorSubject.create<Result>()
  private lateinit var unbinder: Unbinder
  protected val disposable = CompositeDisposable()
  @Inject lateinit var modelFactory: ViewModelProvider.Factory
  lateinit var model: T

  @LayoutRes abstract fun layout(): Int
  abstract fun intents(): V
  abstract fun render(state: U)

  private fun navigate(navigation: Navigation) {
    when (navigation) {
      is GoBack -> activity.onBackPressed()
      is NextScreen -> activity.supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, navigation.screen)
          .addToBackStack("")
          .commit()
      is NextStage -> startActivity(Intent(context, navigation.clazz.java))
      is WithResult ->
        if (navigation.intent.resolveActivity(context.packageManager) != null) {
          startActivityForResult(navigation.intent, navigation.requestCode)
        } else {
          Toast.makeText(context, context.getString(R.string.no_app_error, navigation.intent), Toast.LENGTH_SHORT).show()
          result.onNext(Result(navigation.requestCode))
        }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    model = ViewModelProviders.of(this, modelFactory)[clazz.java]
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
      inflater.inflate(layout(), container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = ButterKnife.bind(this, view)
  }

  override fun onStart() {
    super.onStart()
    disposable.addAll(
        model.state.log("screen state").subscribe { render(it) },
        model.navigation.log("screen navigation").subscribe { navigate(it) },
        model.collectIntents(intents(), result)
    )
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    result.onNext(Result(requestCode, resultCode == Activity.RESULT_OK))
  }

  override fun onStop() {
    disposable.clear()
    super.onStop()
  }
}