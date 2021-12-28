package com.firefly.bikerr.navigation.community.map.bikerr.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DataFactory {


    val URL_PARTS = BASE_URL + "1BQtf0JbAR5Z5STjmT1y1o_P-p_39L8nmbAc5cL82ijM/values/Sheet1!A2:E/"

    val KEY = "AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw"
    companion object{
        private val BASE_URL = "https://sheets.googleapis.com/v4/spreadsheets/"

        fun create(): DataService? {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(DataService::class.java)
        }
    }
}