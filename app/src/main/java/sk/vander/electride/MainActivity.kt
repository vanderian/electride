package sk.vander.electride

import android.os.Bundle
import sk.vander.electride.fragment.RoutesFragment
import sk.vander.lib.ui.FragmentActivity

/**
 * @author marian on 5.9.2017.
 */
class MainActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      fragmentManager.beginTransaction()
          .replace(R.id.container_id, RoutesFragment())
          .addToBackStack("")
          .commit()
    }
  }
}