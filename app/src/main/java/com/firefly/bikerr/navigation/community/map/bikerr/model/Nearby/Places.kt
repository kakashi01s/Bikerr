package com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby


import com.google.gson.annotations.SerializedName

data class Places(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val nextPageToken: String,
    val results: List<Result>,
    val status: String
)