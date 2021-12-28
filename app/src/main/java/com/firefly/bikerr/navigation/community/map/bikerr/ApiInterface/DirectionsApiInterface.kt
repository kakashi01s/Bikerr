package com.firefly.bikerr.navigation.community.map.bikerr.ApiInterface

import android.content.Context
import com.firefly.bikerr.navigation.community.map.bikerr.directions.DirectionResponses
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val direction_base_url = "https://maps.googleapis.com"
interface ApiServices {
    @GET("maps/api/directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("key") apiKey: String): Call<DirectionResponses>
}
//interface ApiServices {
//    @GET("/maps/api/directions/json")
//    fun getMapDirections(
//        @Query("origin") origin: String?,
//        @Query("destination") destination: String?,
//        @Query("key") key: String?
//    ): Call<DirectionResponses?>?
//}

object RetrofitClient {
    fun apiServices(context: Context): ApiServices {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(direction_base_url)
            .build()

        return retrofit.create<ApiServices>(ApiServices::class.java)
    }
}