package com.nb.findfalcone.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.nb.findfalcone.databinding.ItemLayoutBinding
import com.nb.findfalcone.databinding.ItemSpinnerLayoutBinding

class CustomSpinnerAdapter(private val context: Context, items: List<String?>) :
    ArrayAdapter<String>(context, 0, items) {

    private lateinit var binding: ItemSpinnerLayoutBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        binding = ItemSpinnerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        if (getItem(position) != null) {
            binding.tvItem.text = getItem(position)
            /*binding.ivLogo.isVisible = if (data.iconRes != -1) {
                binding.ivLogo.setImageResource(data.iconRes)
                true
            } else false*/
        }
        return binding.root
    }

}