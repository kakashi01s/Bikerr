package com.firefly.bikerr.navigation.community.map.bikerr.ApiInterface

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface StramchatApiInterface {

    @POST("/createchannel")
    fun createchannel(@Body map : HashMap<String , String>) : Call<String>



}