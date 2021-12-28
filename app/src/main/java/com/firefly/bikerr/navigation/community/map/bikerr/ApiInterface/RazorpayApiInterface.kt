package com.firefly.bikerr.navigation.community.map.bikerr.ApiInterface

import com.firefly.bikerr.navigation.community.map.bikerr.model.Order
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RazorpayApiInterface {
    @POST("/getOrderId")
    fun getOrderId(@Body map : HashMap<String, String>): Call<Order>
    @POST("/updateTransaction")
    fun updateTransaction(@Body map : HashMap<String , String>) :Call<String>
    @POST("/sendmail")
    fun sendEmail(@Body map : HashMap<String , String>) : Call<String>
    @POST("/createchannelModerator")
    fun createMod(@Body uid : String): Call<String>
}