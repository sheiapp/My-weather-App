package com.example.remote.model


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("localtime")
    val localtime: String? = null,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Int? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("region")
    var region: String,
    @SerializedName("tz_id")
    val tzId: String? = null
)