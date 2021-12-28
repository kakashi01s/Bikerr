package com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby


import com.google.gson.annotations.SerializedName

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)