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
@Entity(tableName = "userIOT")
data class UserIOT  (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @ColumnInfo(name = "login")
    private var _login: String? = "",

    @ColumnInfo(name = "password")
    private var _password: String? = "",

    @ColumnInfo(name = "firstname")
    private var _firstname: String? = "",

    @ColumnInfo(name = "lastname")
    private var _lastname: String? = "",

    @ColumnInfo(name = "address")
    private var _address: String? = "",

    @ColumnInfo(name = "town")
    private var _town: String? = "",

    @ColumnInfo(name = "country")
    private var _country: String? = "",

    @ColumnInfo(name = "birthday_date")
    private var _birthdayDate: Long = 0,

    @ColumnInfo(name = "gender")
    private var _gender: String? = ""

): Parcelable,
    BaseObservable() {

    var id: Long
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    var login: String?
        @Bindable get() = _login
        set(value) {
            _login = value
            notifyPropertyChanged(BR.login)
        }


    var password: String?
        @Bindable get() = _password
        set(value) {
            _password = value
            notifyPropertyChanged(BR.password)
        }

    var firstname: String?
        @Bindable get() = _firstname
        set(value) {
            _firstname = value
            notifyPropertyChanged(BR.firstname)
        }

    var lastname: String?
        @Bindable get() = _lastname
        set(value) {
            _lastname = value
            notifyPropertyChanged(BR.lastname)
        }

    var address: String?
        @Bindable get() = _address
        set(value) {
            _address = value
            notifyPropertyChanged(BR.address)
        }

    var town: String?
        @Bindable get() = _town
        set(value) {
            _town = value
            notifyPropertyChanged(BR.town)
        }

    var country: String?
        @Bindable get() = _country
        set(value) {
            _country = value
            notifyPropertyChanged(BR.country)
        }

    var birthdayDate: Long
        @Bindable get() = _birthdayDate
        set(value) {
            _birthdayDate = value
            notifyPropertyChanged(BR.birthdayDate)
        }

    var gender: String?
        @Bindable get() = _gender
        set(value) {
            _gender = value
            notifyPropertyChanged(BR.gender)
        }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(login)
        parcel.writeString(password)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(address)
        parcel.writeString(town)
        parcel.writeString(country)
        parcel.writeLong(birthdayDate)
        parcel.writeString(gender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserIOT> {
        override fun createFromParcel(parcel: Parcel): UserIOT {
            return UserIOT(parcel)
        }

        override fun newArray(size: Int): Array<UserIOT?> {
            return arrayOfNulls(size)
        }
    }
}
