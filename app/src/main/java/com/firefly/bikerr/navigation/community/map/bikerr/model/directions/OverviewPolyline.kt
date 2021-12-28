package com.firefly.bikerr.navigation.community.map.bikerr.model.directions

import com.google.gson.annotations.SerializedName

data class OverviewPolyline(
        @SerializedName("points")
        var points: String?
)