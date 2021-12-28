package com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("business_status")
    val businessStatus: String,
    val geometry: Geometry,
    val icon: String,
    val name: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHours,
    val photos: List<Photo>,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCode,
    @SerializedName("price_level")
    val priceLevel: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    val vicinity: String,
    @SerializedName("permanently_closed")
    val permanentlyClosed: Boolean
)