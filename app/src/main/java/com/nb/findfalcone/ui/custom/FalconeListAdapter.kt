package com.nb.findfalcone.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nb.findfalcone.R
import com.nb.findfalcone.databinding.ItemLayoutBinding
import com.nb.findfalcone.model.VehicleToPlanet
import com.nb.findfalcone.ui.utils.getPlanetImage
import com.nb.findfalcone.ui.utils.getVehicleImage

class FalconeListAdapter : ListAdapter<VehicleToPlanet, FalconeListAdapter.ViewHolder>(
    VehicleToPlanet.DiffUtilResponse
) {

    class ViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VehicleToPlanet?) {
            binding.ivVehicle.setImageResource(binding.root.context.getVehicleImage(item?.vehicle?.name))
            binding.ivPlanet.setImageResource(binding.root.context.getPlanetImage(item?.planet?.name))
            binding.tvItem.text = item?.vehicle?.name
            binding.tvPlanet.text = item?.planet?.name ?: "NA"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}