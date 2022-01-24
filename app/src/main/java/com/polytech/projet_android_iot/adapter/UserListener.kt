package com.polytech.projet_android_iot.adapter

import com.polytech.projet_android_iot.model.User

class UserListener(val clickListener: (userid: Long) -> Unit) {
    fun onClick(user: User) = clickListener(user.id)



}

