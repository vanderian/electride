package sk.vander.lib.ui.screen

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.Multibinds
import sk.vander.lib.annotations.ApplicationScope
import javax.inject.Provider

/**
 * @author marian on 21.9.2017.
 */
@Module(includes = arrayOf(ScreenModelModule.MapModule::class))
object ScreenModelModule {

  @Suppress("UNCHECKED_CAST")
  @JvmStatic @Provides @ApplicationScope fun provideModelFactory(
      creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
  ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (creators.containsKey(modelClass)) {
        return creators[modelClass]!!.get() as T
      } else {
        throw IllegalArgumentException("unknown model class " + modelClass)
      }
    }
  }

  @Module
  abstract class MapModule {
    @Multibinds abstract fun provideMap(): Map<Class<out ViewModel>, ViewModel>
  }

}