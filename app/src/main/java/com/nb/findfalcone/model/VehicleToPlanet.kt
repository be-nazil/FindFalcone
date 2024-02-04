package com.nb.findfalcone.model

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil

@Keep
data class VehicleToPlanet(
    val planet: Planet?,
    val vehicle: Vehicle?
) {
    companion object {
        val DiffUtilResponse = object :DiffUtil.ItemCallback<VehicleToPlanet>() {
            override fun areItemsTheSame(
                oldItem: VehicleToPlanet,
                newItem: VehicleToPlanet
            ): Boolean {
                return oldItem.planet?.name == newItem.planet?.name
            }

            override fun areContentsTheSame(
                oldItem: VehicleToPlanet,
                newItem: VehicleToPlanet
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

