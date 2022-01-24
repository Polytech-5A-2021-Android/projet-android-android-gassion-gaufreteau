package com.polytech.projet_android_iot.adapter

import androidx.recyclerview.widget.DiffUtil
import com.polytech.projet_android_iot.model.User

class UserDiffCallback(function: () -> Unit) : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}