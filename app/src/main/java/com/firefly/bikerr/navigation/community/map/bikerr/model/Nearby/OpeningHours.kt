package com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby


import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean
)