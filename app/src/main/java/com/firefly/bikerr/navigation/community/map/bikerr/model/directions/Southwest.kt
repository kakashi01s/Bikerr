package com.firefly.bikerr.navigation.community.map.bikerr.model.directions

import com.google.gson.annotations.SerializedName

data class Southwest(
        @SerializedName("lat")
        var lat: Double?,
        @SerializedName("lng")
        var lng: Double?
)