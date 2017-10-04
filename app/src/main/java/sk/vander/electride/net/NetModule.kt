package sk.vander.electride.net

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sk.vander.electride.net.api.OpenChargeApiService
import sk.vander.lib.annotations.ApplicationScope

@Module
object NetModule {

  @JvmStatic @Provides @ApplicationScope
  fun providesClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

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