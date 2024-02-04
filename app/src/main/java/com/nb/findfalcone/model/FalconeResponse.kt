package com.nb.findfalcone.model

import com.google.gson.annotations.SerializedName

data class FalconeResponse(
    @SerializedName("planet_name")
    var planetName: String?,
    var status: String?,
    var error: String?
)
