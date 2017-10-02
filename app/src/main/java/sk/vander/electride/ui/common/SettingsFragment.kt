package sk.vander.electride.ui.common

import android.os.Bundle
import android.preference.PreferenceFragment
import sk.vander.electride.R

class SettingsFragment: PreferenceFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)
  }
}