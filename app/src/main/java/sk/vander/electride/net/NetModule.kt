package sk.vander.electride.net

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sk.vander.electride.net.api.OpenChargeApiService
import sk.vander.lib.annotations.ApplicationScope

@Module
object NetModule {

  @JvmStatic @Provides @ApplicationScope
  fun providesClient(): OkHttpClient =
      OkHttpClient.Builder()
          //fixme this will fail outside debug, also no logs in release please
          .addNetworkInterceptor(StethoInterceptor())
          .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
          .build()

  @JvmStatic @Provides @ApplicationScope
  fun providesRetrofit(client: OkHttpClient): Retrofit =
      Retrofit.Builder()
          .baseUrl("https://api.openchargemap.io")
          .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
          .addConverterFactory(GsonConverterFactory.create())
          .client(client)
          .build()

  @JvmStatic @Provides @ApplicationScope
  fun providesOpenChargeServiceRetrofit(retrofit: Retrofit): OpenChargeApiService =
      retrofit.create(OpenChargeApiService::class.java)
}