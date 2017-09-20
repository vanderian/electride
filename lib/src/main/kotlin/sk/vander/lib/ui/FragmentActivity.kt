package sk.vander.lib.ui

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.squareup.coordinators.CoordinatorProvider
import com.squareup.coordinators.Coordinators
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import sk.vander.lib.R
import javax.inject.Inject

abstract class FragmentActivity : AppCompatActivity(), HasFragmentInjector {

  @Inject lateinit var viewContainer: ViewContainer
  @Inject lateinit var coordinatorProvider: CoordinatorProvider
  @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
  private lateinit var container: ViewGroup

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)

    // calls setContentView()
    container = viewContainer[this]
    container.setOnHierarchyChangeListener(HierarchyTreeChangeListener.wrap(object : ViewGroup.OnHierarchyChangeListener {
      override fun onChildViewAdded(parent: View, child: View) {
        Coordinators.bind(child, coordinatorProvider)
      }

      override fun onChildViewRemoved(parent: View, child: View) {}
    }))
    val frame = FrameLayout(this)
    frame.id = R.id.container_id
    container.addView(frame)
  }

  override fun fragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}