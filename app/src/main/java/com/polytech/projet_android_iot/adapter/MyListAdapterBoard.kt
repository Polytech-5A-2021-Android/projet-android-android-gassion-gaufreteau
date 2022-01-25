package com.polytech.projet_android_iot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polytech.projet_android_iot.databinding.BoardItemViewBinding
import com.polytech.projet_android_iot.model.BoardIOT

class MyListAdapterBoard(
    private val clickListener: BoardListener
) : ListAdapter<BoardIOT, MyListAdapterBoard.ViewHolder>(BoardDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: BoardItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: BoardIOT, clickListener: BoardListener) {
            binding.board = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BoardItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}