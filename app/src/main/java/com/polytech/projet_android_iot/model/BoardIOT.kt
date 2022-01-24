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
@Entity(tableName = "boardIOT")
data class BoardIOT  (
    @Json(name="id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @Json(name="name")
    @SerializedName("name")
    @ColumnInfo(name = "name")
    private var _name: String? = ""

): Parcelable,
    BaseObservable() {

    var id: Long
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    var name: String?
        @Bindable get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardIOT> {
        override fun createFromParcel(parcel: Parcel): BoardIOT {
            return BoardIOT(parcel)
        }

        override fun newArray(size: Int): Array<BoardIOT?> {
            return arrayOfNulls(size)
        }
    }
}
