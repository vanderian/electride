package sk.vander.electride.net.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import sk.vander.electride.net.model.ChargePoint

interface OpenChargeApiService {

  @GET("/v2/poi/?distanceunit=KM&compact=true&verbose=false")
  fun fetchPoi(
      @Query("latitude") latitude: Double,
      @Query("longitude") longitude: Double,
      @Query("distance") distance: Int
  ): Single<List<ChargePoint>>
}