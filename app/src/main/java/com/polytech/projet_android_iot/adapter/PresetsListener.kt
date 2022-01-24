package com.polytech.projet_android_iot.adapter

import com.polytech.projet_android_iot.model.PresetsIOT

class PresetsListener(val clickListener: (presetid: Long) -> Unit) {
    fun onClick(preset: PresetsIOT) = clickListener(preset.id)
}