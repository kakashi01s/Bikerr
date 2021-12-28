package com.firefly.bikerr.navigation.community.map.bikerr.directions

import com.firefly.bikerr.navigation.community.map.bikerr.model.directions.Northeast
import com.firefly.bikerr.navigation.community.map.bikerr.model.directions.Southwest
import com.google.gson.annotations.SerializedName


data class Bounds(
        @SerializedName("northeast")
        var northeast: Northeast?,
        @SerializedName("southwest")
        var southwest: Southwest?
)