package com.polytech.projet_android_iot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polytech.projet_android_iot.databinding.PresetsItemViewBinding
import com.polytech.projet_android_iot.model.PresetsIOT

class MyListAdapterPresets(
    private val clickListener: PresetsListener
) : ListAdapter<PresetsIOT, MyListAdapterPresets.ViewHolder>(PresetsDiffCallback {}) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: PresetsItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PresetsIOT, clickListener: PresetsListener) {
            binding.preset = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PresetsItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}