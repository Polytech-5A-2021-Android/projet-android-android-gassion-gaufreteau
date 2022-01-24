package com.polytech.projet_android_iot.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.polytech.projet_android_iot.BR
import com.squareup.moshi.Json

@Keep
@Entity(tableName = "presetsIOT")
data class PresetsIOT  (
    @SerializedName("id")
    @Json(name="id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @SerializedName("bid")
    @Json(name="bid")
    @ColumnInfo(name = "bid")
    private var _bid: Long = 0L,

    @SerializedName("name")
    @Json(name="name")
    @ColumnInfo(name = "name")
    private var _name: String? = "",

    @SerializedName("led1")
    @Json(name="led1")
    @ColumnInfo(name = "led1")
    private var _led1: String? = "",

    @SerializedName("led2")
    @Json(name="led2")
    @ColumnInfo(name = "led2")
    private var _led2: String? = "",

    @SerializedName("led3")
    @Json(name="led3")
    @ColumnInfo(name = "led3")
    private var _led3: String? = "",

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

    var led1: String?
        @Bindable get() = _led1
        set(value) {
            _led1 = value
            notifyPropertyChanged(BR.led1)
        }

    var led2: String?
        @Bindable get() = _led2
        set(value) {
            _led2 = value
            notifyPropertyChanged(BR.led2)
        }

    var led3: String?
        @Bindable get() = _led3
        set(value) {
            _led3 = value
            notifyPropertyChanged(BR.led3)
        }


    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(bid)
        parcel.writeString(name)
        parcel.writeString(led1)
        parcel.writeString(led2)
        parcel.writeString(led3)
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
