package com.firefly.bikerr.navigation.community.map.bikerr.model.directions

import com.google.gson.annotations.SerializedName

data class Distance(
        @SerializedName("text")
        var text: String?,
        @SerializedName("value")
        var value: Int?
)