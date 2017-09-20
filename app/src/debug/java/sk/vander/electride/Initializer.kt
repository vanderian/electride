package sk.vander.electride

import sk.vander.lib.BaseAppModule

/**
 * @author marian on 5.9.2017.
 */
object Initializer {
  fun init(app: App): AppComponent =
      DaggerAppComponent.builder()
          .baseAppModule(BaseAppModule(app))
          .build()
}