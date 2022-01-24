package com.polytech.projet_android_iot.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polytech.projet_android_iot.BR

@Keep
@Entity(tableName = "presetsIOT")
data class PresetsIOT  (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @ColumnInfo(name = "bid")
    private var _bid: Long = 0L,

    @ColumnInfo(name = "name")
    private var _name: String? = "",

    @ColumnInfo(name = "led1")
    private var _led1: Int = 0,

    @ColumnInfo(name = "led2")
    private var _led2: Int = 0,

    @ColumnInfo(name = "led3")
    private var _led3: Int = 0,

    ): Parcelable,
    BaseObservable() {

    var id: Long
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    var bid: Long
        @Bindable get() = _bid
        set(value) {
            _bid = value
            notifyPropertyChanged(BR.bid)
        }

    var name: String?
        @Bindable get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }

    var led1: Int
        @Bindable get() = _led1
        set(value) {
            _led1 = value
            notifyPropertyChanged(BR.led1)
        }

    var led2: Int
        @Bindable get() = _led2
        set(value) {
            _led2 = value
            notifyPropertyChanged(BR.led2)
        }

    var led3: Int
        @Bindable get() = _led3
        set(value) {
            _led3 = value
            notifyPropertyChanged(BR.led3)
        }


    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(bid)
        parcel.writeString(name)
        parcel.writeInt(led1)
        parcel.writeInt(led2)
        parcel.writeInt(led3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresetsIOT> {
        override fun createFromParcel(parcel: Parcel): PresetsIOT {
            return PresetsIOT(parcel)
        }

        override fun newArray(size: Int): Array<PresetsIOT?> {
            return arrayOfNulls(size)
        }
    }
}
