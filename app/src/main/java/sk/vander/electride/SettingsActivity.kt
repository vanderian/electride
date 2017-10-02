package sk.vander.electride

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import sk.vander.electride.ui.common.SettingsFragment

class SettingsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    fragmentManager.beginTransaction()
        .replace(android.R.id.content, SettingsFragment())
        .commit()
  }
}