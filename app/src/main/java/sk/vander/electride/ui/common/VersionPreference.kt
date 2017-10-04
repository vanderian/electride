package sk.vander.electride.ui.common

import android.content.Context
import android.preference.EditTextPreference
import android.util.AttributeSet
import android.view.View
import sk.vander.electride.BuildConfig

class VersionPreference: EditTextPreference {
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?) : super(context)

  override fun onClick() {}

  override fun onBindView(view: View?) {
    super.onBindView(view)
    summary = BuildConfig.VERSION_NAME
  }
}