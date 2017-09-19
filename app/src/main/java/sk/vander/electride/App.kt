package sk.vander.electride

import sk.vander.lib.BaseApp

/**
 * @author marian on 5.9.2017.
 */
@BuildTypeComponent
abstract class App : BaseApp() {
  override fun buildComponentAndInject() = Initializer.init(this).inject(this)
}