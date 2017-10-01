package sk.vander.electride

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import sk.vander.electride.service.LocationService
import sk.vander.electride.ui.routes.RoutesScreen
import sk.vander.lib.ui.FragmentActivity

/**
 * @author marian on 5.9.2017.
 */
class MainActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, RoutesScreen())
          .commit()
    }

    ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java))

    RxPermissions(this).request(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION).subscribe {
      if (!it) finish()
    }
  }
}