package com.firefly.bikerr.navigation.community.map.bikerr.directions

import com.firefly.bikerr.navigation.community.map.bikerr.model.directions.GeocodedWaypoint
import com.firefly.bikerr.navigation.community.map.bikerr.model.directions.Route
import com.google.gson.annotations.SerializedName

data class DirectionResponses(
        @SerializedName("geocoded_waypoints")
        var geocodedWaypoints: List<GeocodedWaypoint?>?,
        @SerializedName("routes")
        var routes: List<Route?>?,
        @SerializedName("status")
        var status: String?
)