package com.polytech.projet_android_iot.adapter

import androidx.recyclerview.widget.DiffUtil
import com.polytech.projet_android_iot.model.BoardIOT

class BoardDiffCallback : DiffUtil.ItemCallback<BoardIOT>() {
    override fun areItemsTheSame(oldItem: BoardIOT, newItem: BoardIOT): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BoardIOT, newItem: BoardIOT): Boolean {
        return oldItem == newItem
    }
}