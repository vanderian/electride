package sk.vander.electride

import sk.vander.electride.screen.Routes
import sk.vander.lib.ui.BaseActivity
import sk.vander.lib.ui.LayoutKey

/**
 * @author marian on 5.9.2017.
 */
class MainActivity : BaseActivity() {
  override fun defaultKey(): LayoutKey = Routes()
}