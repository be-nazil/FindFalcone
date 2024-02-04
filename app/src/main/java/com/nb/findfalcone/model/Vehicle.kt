package com.nb.findfalcone.model

import com.google.gson.annotations.SerializedName

data class Vehicle(
    var name: String?,
    @SerializedName("total_no")
    var totalNo: Int?,
    @SerializedName("max_distance")
    var maxDistance: Int?,
    var speed: Int?,
)
