package com.nb.findfalcone.model

data class FalconeRequest(
    val token: String,
    val planet_names: List<String?>,
    val vehicle_names: List<String?>
)
