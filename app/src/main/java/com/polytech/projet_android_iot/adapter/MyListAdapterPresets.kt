package com.polytech.projet_android_iot.adapter

import android.graphics.Color
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

            binding.tvColor1.setBackgroundColor(parseColor(item.led1))
            binding.tvColor2.setBackgroundColor(parseColor(item.led2))
            binding.tvColor3.setBackgroundColor(parseColor(item.led3))
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        fun parseColor(str: String?): Int {
            if(str.isNullOrEmpty()) {
                return Color.WHITE
            }
            return Color.parseColor(str)
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