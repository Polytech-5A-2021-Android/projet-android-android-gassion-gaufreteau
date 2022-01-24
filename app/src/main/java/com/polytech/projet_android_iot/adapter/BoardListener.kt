package com.polytech.projet_android_iot.adapter

import com.polytech.projet_android_iot.model.BoardIOT

class BoardListener(val clickListener: (boardid: Long) -> Unit) {
    fun onClick(board: BoardIOT) = clickListener(board.id)
}