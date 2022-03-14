package com.example.remote.model


import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("icon")
    var icon: String? = null,
    @SerializedName("text")
    var text: String? = null
)