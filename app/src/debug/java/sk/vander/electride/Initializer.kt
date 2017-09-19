package sk.vander.electride

/**
 * @author marian on 5.9.2017.
 */
object Initializer {
  fun init(app: App): AppComponent = DaggerAppComponent.create()
}