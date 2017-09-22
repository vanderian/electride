package sk.vander.electride

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import sk.vander.electride.service.LocationService
import sk.vander.electride.ui.routes.RoutesFragment
import sk.vander.lib.ui.FragmentActivity

/**
 * @author marian on 5.9.2017.
 */
class MainActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, RoutesFragment())
          .commit()
    }

    ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java))
  }
}