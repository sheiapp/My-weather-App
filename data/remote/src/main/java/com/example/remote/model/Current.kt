package com.example.remote.model


import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("cloud")
    val cloud: Int? = null,
    @SerializedName("condition")
    val condition: Condition,
    @SerializedName("feelslike_c")
    val feelslikeC: Double? = null,
    @SerializedName("feelslike_f")
    val feelslikeF: Double? = null,
    @SerializedName("gust_kph")
    val gustKph: Double? = null,
    @SerializedName("gust_mph")
    val gustMph: Double? = null,
    @SerializedName("humidity")
    var humidity: Int? = null,
    @SerializedName("is_day")
    val isDay: Int? = null,
    @SerializedName("last_updated")
    val lastUpdated: String? = null,
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Int? = null,
    @SerializedName("precip_in")
    var precipIn: Double? = null,
    @SerializedName("precip_mm")
    val precipMm: Double? = null,
    @SerializedName("pressure_in")
    val pressureIn: Double? = null,
    @SerializedName("pressure_mb")
    val pressureMb: Double? = null,
    @SerializedName("temp_c")
    var tempC: Double? = null,
    @SerializedName("temp_f")
    var tempF: Double? = null,
    @SerializedName("uv")
    val uv: Double? = null,
    @SerializedName("vis_km")
    var visKm: Double? = null,
    @SerializedName("vis_miles")
    val visMiles: Double? = null,
    @SerializedName("wind_degree")
    val windDegree: Int? = null,
    @SerializedName("wind_dir")
    val windDir: String? = null,
    @SerializedName("wind_kph")
    var windKph: Double? = null,
    @SerializedName("wind_mph")
    val windMph: Double? = null
)