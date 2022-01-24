package com.polytech.projet_android_iot.adapter

import androidx.recyclerview.widget.DiffUtil
import com.polytech.projet_android_iot.model.PresetsIOT

class PresetsDiffCallback(function: () -> Unit) : DiffUtil.ItemCallback<PresetsIOT>() {
    override fun areItemsTheSame(oldItem: PresetsIOT, newItem: PresetsIOT): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PresetsIOT, newItem: PresetsIOT): Boolean {
        return oldItem == newItem
    }
}